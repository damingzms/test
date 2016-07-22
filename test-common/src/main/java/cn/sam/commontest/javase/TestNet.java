package cn.sam.commontest.javase;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestNet {
	
	public static void main(String[] args) throws UnknownHostException {
		System.out.println(InetAddress.getLocalHost());
		System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
	}

}
