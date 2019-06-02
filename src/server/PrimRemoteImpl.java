package server;

import client.PrimRemote;
import utils.AdjMatrix;
import utils.PrimEdge;

import java.rmi.*;
import java.rmi.server.*;


public class PrimRemoteImpl extends UnicastRemoteObject implements PrimRemote {

    public PrimRemoteImpl() throws RemoteException { }

    @Override
    public AdjMatrix updateDTable(int newNode, AdjMatrix matrix) throws RemoteException {
        matrix.addDoneNode(newNode);
        return matrix;
    }

    @Override
    public PrimEdge calculateMinimum(AdjMatrix matrix) throws RemoteException {
        return matrix.calculateMinimum();
    }
}
