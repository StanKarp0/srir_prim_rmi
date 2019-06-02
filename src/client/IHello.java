package client;

import java.rmi.*;


public interface IHello extends Remote
{
    public String getGreetingMessage() throws RemoteException;
}
