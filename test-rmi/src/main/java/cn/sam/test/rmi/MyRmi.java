package cn.sam.test.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyRmi extends Remote {
	public int add(int i, int j) throws RemoteException;
}
