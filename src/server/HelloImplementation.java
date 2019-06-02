package server;

import client.IHello;

import java.rmi.*;
import java.rmi.server.*;

public class HelloImplementation extends UnicastRemoteObject
        implements IHello {

    public HelloImplementation() throws RemoteException {
//There is no action need in this moment.
    }
    @Override
    public String getGreetingMessage() throws RemoteException {
        return ("Hello there, student.");
    }

}
