package client;

import utils.AdjMatrix;
import utils.PrimResult;

import java.rmi.*;


public interface PrimRemote extends Remote
{
    public PrimResult calculateMinimum(AdjMatrix matrix) throws RemoteException;
}
