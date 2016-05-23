package cn.sam.test.rmi_client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import cn.sam.test.rmi.MyRmi;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
		MyRmi service;
		try {
			service = (MyRmi) Naming.lookup("rmi://localhost:8888/jsRmi");
			int result = service.add(4, 7);
			System.out.println(result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
