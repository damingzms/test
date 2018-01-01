package com.twitter.finagle.example.java.thrift;

import com.twitter.finagle.example.thriftjava.Hello;
import com.twitter.util.Future;

public class HelloImpl implements Hello.ServiceIface {
	
	public Future<String> hi() {
		return Future.value("hi");
	}
	
}