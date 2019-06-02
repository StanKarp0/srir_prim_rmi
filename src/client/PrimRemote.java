package client;

import java.rmi.*;


public interface PrimRemote extends Remote
{
    public String getGreetingMessage() throws RemoteException;
}
