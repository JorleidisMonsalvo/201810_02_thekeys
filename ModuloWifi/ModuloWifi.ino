#include <PubSubClient.h>
#include <ESP8266WiFi.h>
#include <string.h>

//DEFINES
#define TOPIC_SUBSCRIBE        "claves.conjunto1.1-101"
#define TOPIC_PUBLISH_1        "alarma.conjunto1.alta.1-101.pre"
#define TOPIC_PUBLISH_2        "alarma.conjunto1.baja.1-101.pre"
#define TOPIC_HEALTHCHECK      "healthcheck.conjunto1.1-101"
#define SIZE_BUFFER_DATA       50

//VARIABLES
const char* idDevice = "ISIS2503ESXASDA";
boolean     stringComplete = false;
boolean     init_flag = false;
String      inputString = "";
char        bufferData [SIZE_BUFFER_DATA];

// CLIENTE WIFI & MQTT
WiFiClient    clientWIFI;
PubSubClient  clientMQTT(clientWIFI);

// CONFIG WIFI
const char* ssid = "FORERO";
const char* password = "6764435r";

// CONFIG MQTT
IPAddress serverMQTT (192,168,0,7);
const uint16_t portMQTT = 1883;
// const char* usernameMQTT = "admin";
// const char* passwordMQTT = "admin";

void connectWIFI() {
  // Conectar a la red WiFi
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  if(WiFi.status() != WL_CONNECTED) {
    WiFi.begin(ssid, password);
  }

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println(WiFi.localIP());
}

void reconnectWIFI() {
  // Conectar a la red WiFi
  if(WiFi.status() != WL_CONNECTED) {
    WiFi.begin(ssid, password);
  }

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  //Serial.println("Callback");
  //Serial.println(length);
  Serial.write(payload, length);
  Serial.println();
}

void setup() {
  Serial.begin(9600);
  WiFi.mode(WIFI_STA);
  inputString.reserve(100);
  connectWIFI();
  clientMQTT.setServer(serverMQTT, portMQTT);
  clientMQTT.setCallback(callback);
  
  delay(1000);
}

void processData() {
  
  if (WiFi.status() == WL_CONNECTED) {
    if(init_flag == false) {
      init_flag = true;

      boolean conectMQTT = false;
      if (!clientMQTT.connected()) {
        // if (!clientMQTT.connect(idDevice, usernameMQTT, passwordMQTT)) {
        if (!clientMQTT.connect(idDevice)) {
          conectMQTT = false;
        }
        conectMQTT = true;
      }
      else {
        conectMQTT = true;
      }

      if(conectMQTT) {
        if(clientMQTT.subscribe(TOPIC_SUBSCRIBE)) {
           //Serial.println("Subscribe OK1");
        }
        if(clientMQTT.subscribe(TOPIC_HEALTHCHECK)) {
           //Serial.println("Subscribe OK2");
        }
      }
    }

    if (stringComplete && clientMQTT.connected()) {
      if(inputString.startsWith("1")) {
        if(clientMQTT.publish(TOPIC_PUBLISH_2, bufferData)) {
          inputString = "";
          stringComplete = false;
        }
      }
      else if(inputString.startsWith("0")) 
      {
        if(clientMQTT.publish(TOPIC_HEALTHCHECK, bufferData)) {
          inputString = "";
          stringComplete = false;
        }
      }
      else
      {        
        if(clientMQTT.publish(TOPIC_PUBLISH_1, bufferData)) {
          inputString = "";
          stringComplete = false;
        }
      }
      init_flag = false;
    }
  }
  else {
    connectWIFI();
    init_flag = false;
  }
   if (!clientMQTT.connected()) {
    reconnect();
  }
  clientMQTT.loop();
}

void receiveData() {
  
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    if (inChar == '\n') {
      inputString.toCharArray(bufferData, SIZE_BUFFER_DATA);
      stringComplete = true;
    }
  }
}
void reconnect() {
  // Loop until we're reconnected
  while (!clientMQTT.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (clientMQTT.connect(idDevice)) {
      if(clientMQTT.subscribe(TOPIC_SUBSCRIBE)) {
           //Serial.println("Subscribe OK1");
        }
        if(clientMQTT.subscribe(TOPIC_HEALTHCHECK)) {
           //Serial.println("Subscribe OK2");
        }
      Serial.println("connected");
      // Once connected, publish an announcement...
      //client.publish("outTopic", "hello world");
      // ... and resubscribe
      //client.subscribe("inTopic");
    } else {
      Serial.print("failed, rc=");
      Serial.print(clientMQTT.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
void loop() {
  receiveData();
  processData();
}
