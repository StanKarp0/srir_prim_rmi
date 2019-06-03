package server;

import client.PrimRemote;
import utils.AdjacencyList;
import utils.PrimEdge;

import java.rmi.*;
import java.rmi.server.*;


public class PrimRemoteImpl extends UnicastRemoteObject implements PrimRemote {

    public PrimRemoteImpl() throws RemoteException { }

    @Override
    public AdjacencyList updateDTable(int newNode, AdjacencyList list) throws RemoteException {
        list.addDoneNode(newNode);
        return list;
    }

    @Override
    public PrimEdge calculateMinimum(AdjacencyList list) throws RemoteException {
        return list.calculateMinimum();
    }
}
