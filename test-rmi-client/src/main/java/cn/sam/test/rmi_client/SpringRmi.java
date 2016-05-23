package cn.sam.test.rmi_client;

import java.rmi.RemoteException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.sam.test.rmi.MyRmi;

public class SpringRmi {

	/**
	 * 使用Spring发布RMI服务端，需要把服务端实现类的"extends UnicastRemoteObject"去掉，否则报：object already exporter
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		MyRmi service = (MyRmi) context.getBean("service");
		System.out.println(service.add(6, 9));
	}

}
