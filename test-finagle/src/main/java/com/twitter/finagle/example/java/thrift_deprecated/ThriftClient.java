package com.twitter.finagle.example.java.thrift_deprecated;

import scala.runtime.BoxedUnit;

import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.Hello;
import com.twitter.util.Await;
import com.twitter.util.Function;
import com.twitter.util.Future;

public final class ThriftClient {

	private ThriftClient() {
	}

	public static void main(String[] args) throws Exception {
		Hello.ServiceIface client = Thrift.client().newIface("localhost:8080", Hello.ServiceIface.class);
		Future<String> response = client.hi().onSuccess(new Function<String, BoxedUnit>() {
			@Override
			public BoxedUnit apply(String response) {
				System.out.println("Received response: " + response);
				return null;
			}
		});

		// 防止进程在响应返回之前结束
		Await.ready(response);
	}
}
