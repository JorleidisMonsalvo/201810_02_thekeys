package mqtt;

public class Principal {

    public static void main(String[] args) {

        Subscriber client = new Subscriber();

        client.start();

        while (true) {}

    }

}