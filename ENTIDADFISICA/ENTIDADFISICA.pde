#include <EEPROM.h>
#include <Keypad.h>
#include <string.h>

int redPin= 13;
int greenPin = 12;
int bluePin = 10;
int ledPin = 14;                // choose the pin for the LED  
int inputPin = 2;               // choose the input pin (for PIR sensor)
int pirState = LOW;             // we start, assuming no motion detected
int val = 0; 
boolean bateria=false;
long currTimeBat;
boolean stringComplete = false;

//Minimum voltage required for an alert
const double MIN_VOLTAGE = 1.2;

//Battery measure pin
const int BATTERY_PIN = A0;

//Battery indicator
const int BATTERY_LED = 15;

//Current battery charge
double batteryCharge;


//Specified password
#define SIZE_BUFFER_DATA       150
String keys[20]={"Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y","Y"};
int numeroClaves=0;
String inputString ="";
char        bufferData [SIZE_BUFFER_DATA];
int contaKeys;
int contadorParcial;
//Time in milliseconds which the system is locked
const int LOCK_TIME = 30000;

//Keypad rows
const byte ROWS = 4; 

//Keypad columns
const byte COLS = 3;

//Maximum number of attempts allowed
const byte maxAttempts = 3;

//Keypad mapping matrix
char hexaKeys[ROWS][COLS] = {
{
'1', '2', '3'
}
,
{
'4', '5', '6'
}
,
{
'7', '8', '9'
}
,
{
'*', '0', '#'
}
};

//Keypad row pins definition
byte rowPins[ROWS] = {
9, 8, 7, 6
}; 

//Keypad column pins definition
byte colPins[COLS] = {
5, 4, 3
};

//Keypad library initialization
Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS); 

//Current key variable
String currentKey;
//Door state
boolean open;
boolean openKey;
//Number of current attempts
byte attempts;
//If the number of current attempts exceeds the maximum allowed
boolean block;

//Button pin
const int CONTACT_PIN = 11;


//Attribute that defines the button state
boolean buttonState;

//Current time when the button is tapped
long currTime;
long currTime2;

//conteo tiempo
long timeHearth=0;
//Intervalo Healthcheck
long intervalo=3000;

void setup() {

        
pinMode(redPin, OUTPUT);
pinMode(greenPin, OUTPUT);
pinMode(bluePin, OUTPUT);
pinMode(ledPin, OUTPUT);      // declare LED as output
pinMode(inputPin, INPUT);     // declare sensor as input
pinMode(CONTACT_PIN,INPUT);

Serial.begin(9600);
setColor(0, 0, 255);
buttonState = false;
currentKey = "";
open = false;
        openKey=false;
attempts = 0;
block = false;
        contadorParcial=0;
        currTimeBat=0;
        
         // Ouput pin definition for BATTERY_LED
  pinMode(BATTERY_LED,OUTPUT);

  //Input pin definition for battery measure
  pinMode(BATTERY_PIN,INPUT);
}


void loop() {  
   
  receiveData();  
  processData();
  readAllKeys();
  numeroClaves=  EEPROM.read(80);
  ///////////////////////////////////////Battery////////////////////////////////////////////////////////
  
  batteryCharge = (analogRead(BATTERY_PIN)*5.4)/1024;

      digitalWrite(BATTERY_LED,HIGH);    
  //Measured value comparison with min voltage required
  if(batteryCharge<=MIN_VOLTAGE) {
    digitalWrite(BATTERY_LED,HIGH);
    Serial.println("4");
    bateria=true;

  }
  else {
    digitalWrite(BATTERY_LED,LOW);
    bateria=false;

  }
  if(bateria)
{
    
    if(millis()-currTimeBat>=30000)
  {
    currTimeBat=millis();
  setColor(255,0,0);
  delay(2000);
  setColor(0,0,255);    
  }
  
}
  

//////////////////////////////////////////////////////////////////////////////PIR///////////////////////
val = digitalRead(inputPin);  // read input value
if (val == HIGH) {            // check if the input is HIGH
digitalWrite(ledPin, HIGH);  // turn LED ON
if (pirState == LOW) {
// we have just turned on
Serial.println("1");
Serial.println(inputString);
// We only want to print on the output change, not state
pirState = HIGH;
}
} else {
digitalWrite(ledPin, LOW); // turn LED OFF
if (pirState == HIGH){
// we have just turned of
//Serial.println("Motion ended!");
// We only want to print on the output change, not state
pirState = LOW;
}
}  

//////////////////////////////////////////////////////////////////////////////////////////KEYPAD////////////////////////

char customKey;

if(!block) {
//Selected key parsed;
customKey = customKeypad.getKey();
}
else {
//Serial.println("Number of attempts exceeded");
while(true);
}

//Verification of input and appended value
if (customKey) {  
currentKey+=String(customKey);
//Serial.println(currentKey);
}

//If the current key contains '*' and door is open
if(open && currentKey.endsWith("*")) {
setColor(0,0,255); 
open = false;
                openKey=false;
//Serial.println("Door closed *");
currentKey = "";
contadorParcial=0;
}
//If the current key contains '#' reset attempt
if(currentKey.endsWith("#")&&currentKey.length()<=4) {
currentKey = "";
//Serial.println("Attempt deleted");
}

//If current key matches the key length
if (currentKey.length()== keys[contaKeys].length()) {
boolean findit=false;
              for(contaKeys=0;(contaKeys<20)&&!findit;contaKeys++)
      {
            
       
if(currentKey.equals(keys[contaKeys])) {
  findit=true;
    
open = true;
                                openKey=true;
//Serial.println("Door opened!!");
setColor(0, 255, 0);
attempts = 0;
currentKey = "";
currTime2=millis();

}
                        else
                        {
                        contadorParcial++;
//                        currentKey = "";
                        }
             }
             contaKeys=0;
}
else if(currentKey.length()> keys[contaKeys].length()){
//Serial.println("Door opened!!");
}  
                if(contadorParcial==20) 
                {
attempts++;
currentKey = "";
setColor(255,0,0);
delay(1000);
setColor(0,0,255);
//Serial.println("Number of attempts: "+String(attempts));
                        contadorParcial=0;
}


//puerta abierta mas de 30 segundos
if(open&&((millis()-currTime2)>=30000)&&openKey) {
setColor(255, 0, 0);
                currTime2=0;
Serial.println("3");
}

if(attempts>=maxAttempts) {
currentKey = "";
attempts = 0;
setColor(255,0,0);
Serial.println("2"); 
for(int i =0;i<30000;i+=intervalo)
{
delay(intervalo);
Serial.println("0"); 
}
//delay(LOCK_TIME);
//Serial.println("System unlocked");
setColor(0,0,255);
}

delay(100);

//////////////////////////////////////////////////////////////////CONTACT//////////////////////////////

//Button input read and processing 
if(!buttonState) 
{
if(digitalRead(CONTACT_PIN)) 
{
currTime = millis();
buttonState = true;
setColor(0, 255, 0);
open = true;
attempts = 0;
//Serial.println("Puerta abierta Manua!!");
}
}
else 
{
if(digitalRead(CONTACT_PIN)) 
{
if((millis()-currTime)>=30000) 
{
setColor(255, 0, 0);
Serial.println("3");
}
}
else
{
setColor(0, 0, 255);
open = false;
buttonState = false;
//Serial.println("Door closed!!");
}
}

//////////////////////////////////////////////////////////////////HEALTHCHECK//////////////////////////////

healthCheck();

delay(100);

}

void setColor(int redValue, int greenValue, int blueValue) {
analogWrite(redPin, redValue);
analogWrite(greenPin, greenValue);
analogWrite(bluePin, blueValue);
}

void healthCheck()
{
if((millis()-timeHearth)>intervalo)
  {
  Serial.println("0");
      timeHearth=millis();
  }
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

void processData()
{
      
  numeroClaves=  EEPROM.read(80);
 if(stringComplete)
{  
  if(inputString.equals("DELETEALL"))
  {
    
  setAllKeys();
  EEPROM.write(80, 0);
  }
  else if(inputString.startsWith("CREATE"))//CREATE:1234
  {
   
    if(numeroClaves<20)
  {
    String newkey=inputString.substring(7);
  changeKey(numeroClaves+1,newkey);
  EEPROM.write(80, ++numeroClaves);
  }
  
  }
  else if(inputString.startsWith("DELETE"))//DELETE:1-20
  {
        
  String numkey= inputString.substring(7);
  changeKey(numkey.toInt(),"XXXX");
    EEPROM.write(80, --numeroClaves);
  }
   else if(inputString.startsWith("UPDATE"))//UPDATE:1234:1-20
  {
      
  String newkey= inputString.substring(7,11);
    String numkey= inputString.substring(12);
  changeKey(numkey.toInt(),newkey);
  }
  else if(inputString.startsWith("HEALTHCHECK"))
{
  String newkey=inputString.substring(12);
  intervalo=newkey.toInt()*1000;
}
   else if(inputString.equals("SHOW"))
  {
    for(int i=0;i<20;i++)
  {
      Serial.print("Clave ");
            Serial.print(i);
                  Serial.print(": ");
  Serial.println(keys[i]);
  }
  }
  inputString="";
  stringComplete = false;
}
}


//ejecutar 1 vez
void setAllKeys()
{
  for(int i=0;i<80;i++)
{
  EEPROM.write(i, 'X');
}

}
void changeKey(int numKey,String key)
{
  keys[numKey--]=key;
  char keyarray[5]={'X','X','X','X','X'};
  int unoacuatro=0;
    
 
  key.toCharArray(keyarray,5);
      
  if(numKey>0)
  {
  numKey*=4;
  }
  for(int i=numKey;i<numKey+4;i++)
  {
    EEPROM.write(i, keyarray[unoacuatro]);
    unoacuatro++;
  }
}

void readAllKeys()
{
  String valor="";
  int contador=0;
  for(int i=0;i<80;i++)
  {
    char a=EEPROM.read(i);    
    valor+=a;
    contador++;
    if(contador==4)
    {
      keys[((i+1)/4)-1]=valor;
      contador=0;
      valor="";
    }
  }
}
