package com.twitter.finagle.example.java.thriftWithZk;

import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.LoggerService;
import com.twitter.util.Await;
import com.twitter.util.Function;
import com.twitter.util.Future;

import scala.runtime.BoxedUnit;

public final class ThriftClient {

	private ThriftClient() {
	}

	public static void main(String[] args) throws Exception {
		LoggerService.ServiceIface client = Thrift.client().build("localhost:8080", LoggerService.ServiceIface.class);//newIface("localhost:8080", LoggerService.ServiceIface.class);
		Future<String> response = client.log("插入订单", 2).onSuccess(new Function<String, BoxedUnit>() {
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
