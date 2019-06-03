package client;

import utils.AdjacencyList;
import utils.PrimEdge;

import java.rmi.*;


public interface PrimRemote extends Remote {

    AdjacencyList updateDTable(int newNode, AdjacencyList list) throws RemoteException;

    PrimEdge calculateMinimum(AdjacencyList list) throws RemoteException;

}
