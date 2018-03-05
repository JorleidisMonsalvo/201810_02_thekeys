package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import java.lang.Throwable;

public class SubscribeCallback implements MqttCallback {

     @Override
     public void connectionLost(Throwable cause) {}

     @Override
     public void messageArrived(String topic, MqttMessage message) {
          System.out.println(topic + ": " + message.toString());
     }

     @Override
     public void deliveryComplete(IMqttDeliveryToken token) {}

}