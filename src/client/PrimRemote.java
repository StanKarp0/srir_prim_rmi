package client;

import utils.AdjMatrix;
import utils.PrimEdge;

import java.rmi.*;


public interface PrimRemote extends Remote {

    AdjMatrix updateDTable(int newNode, AdjMatrix matrix) throws RemoteException;

    PrimEdge calculateMinimum(AdjMatrix matrix) throws RemoteException;

}
