package server;

import client.PrimRemote;

import java.rmi.*;
import java.rmi.server.*;

public class HelloImplementation extends UnicastRemoteObject
        implements PrimRemote {

    public HelloImplementation() throws RemoteException {
//There is no action need in this moment.
    }
    @Override
    public String getGreetingMessage() throws RemoteException {
        return ("Hello there, student.");
    }

}
