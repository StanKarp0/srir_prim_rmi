package server;

import utils.Common;

import java.rmi.*;

public class PrimServerComponent {

    public static void main(String[] args) throws Exception {
        int remote = Integer.parseInt(args[0]);

        //** Step 1
        //** Declare a reference for the object that will be implemented
        PrimRemoteImpl temp = new PrimRemoteImpl();
        //** Step 2
        //** Declare a string variable for holding the URL of the object's name
        String rmiObjectName = Common.getServerPath(remote);
        //Step 3
        //Binding the object reference to the object name.
        Naming.rebind(rmiObjectName, temp);
        //Step 4
        //Tell to the user that the process is completed.
        System.out.println("Binding complete...\n");

    }
}