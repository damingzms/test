package com.twitter.finagle.example.java.thrift;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.Hello;
import com.twitter.util.Await;
import com.twitter.util.Future;
import com.twitter.util.TimeoutException;

/**
 * 需要设置scrooge-maven-plugin插件，根据IDL（*.thrift）生成java代码。见pom.xml文件
 *
 * Scrooge is meant to be a replacement for the Apache Thrift code generator, and generates conforming, binary-compatible codecs by building on top of libthrift.
 * 为Scrooge设置--finagle参数，可以根据IDL生成finagle的thrift协议的代码。见pom.xml文件
 */
public final class ThriftServer {

	private ThriftServer() {
	}

	public static class HelloImpl implements Hello.ServiceIface {
		public Future<String> hi() {
			return Future.value("hi");
		}
	}

	public static void main(String[] args) throws TimeoutException, InterruptedException {
		Hello.ServiceIface impl = new HelloImpl();
		ListeningServer server = Thrift.server().serveIface("localhost:8080", impl);
		Await.ready(server);
	}
}
