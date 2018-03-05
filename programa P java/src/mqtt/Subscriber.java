package mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class Subscriber {

     public static final String BROKER_URL = "tcp://172.24.42.79:8083";
     private MqttClient client;

     public Subscriber() {

          String clientId = "demonio";
          try {
              client = new MqttClient(BROKER_URL, clientId);
          }
          catch (MqttException e) {
              e.printStackTrace();
              System.exit(1);
          }
     }

     public void start() {

          try {
              client.setCallback(new SubscribeCallback());
              client.connect();
              client.subscribe("/baja/Conjunto1/1/A101");
              client.subscribe("/alta/Conjunto1/1/A101");
              System.out.println(client.getTopic("/alta/Conjunto1/1/A101"));
              System.out.println(client.isConnected());
          }
          catch (MqttException e) {
              e.printStackTrace();
              System.exit(1);
          }

     }

}