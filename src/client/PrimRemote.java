package client;

import utils.AdjMatrix;
import utils.PrimResult;

import java.rmi.*;


public interface PrimRemote extends Remote {

    AdjMatrix updateDTable(int newNode, AdjMatrix matrix) throws RemoteException;

    PrimResult calculateMinimum(AdjMatrix matrix) throws RemoteException;

}
