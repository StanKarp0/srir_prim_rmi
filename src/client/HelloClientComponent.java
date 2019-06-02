package client;

import utils.CsvReader;

import java.rmi.*;
import java.util.Arrays;

public class HelloClientComponent {
    private static final String host = "localhost";

    public static void main(String[] args) {
        try {

            CsvReader reader = new CsvReader("../resources/nodes6edges12.csv", ",");
            reader.readLines().forEach(a -> {
                System.out.println(Arrays.toString(a));
            });

        //We obtain a reference to the object from the registry and next,
        //it will be typecasted into the most appropiate type.
            PrimRemote greeting_message = (PrimRemote) Naming.lookup("rmi://"
                    + host + "/Hello");
        //Next, we will use the above reference to invoke the remote
        //object method.
            System.out.println("Message received: " +
                    greeting_message.getGreetingMessage());
        } catch (ConnectException conEx) {
            System.out.println("Unable to connect to server!");
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}