package cn.sam.test.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 使用Spring发布RMI服务端，需要把"extends UnicastRemoteObject"去掉
 * @author JoJo
 *
 */
public class MyRmiImpl extends UnicastRemoteObject implements MyRmi {

	protected MyRmiImpl() throws RemoteException {
		super();
	}

	public int add(int i, int j) throws RemoteException {
		return i + j;
	}

	public static void main(String[] args) {
        try {
//        	System.setProperty("java.rmi.server.hostname", "127.0.0.1");
//        	System.setSecurityManager(new java.rmi.RMISecurityManager());
    		LocateRegistry.createRegistry(8888);
    		MyRmiImpl service = new MyRmiImpl();
//			Naming.rebind("jsRmi", service);
			Naming.bind("rmi://localhost:8888/jsRmi",service);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

}
