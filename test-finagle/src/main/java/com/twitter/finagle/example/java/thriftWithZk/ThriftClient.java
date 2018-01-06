package com.twitter.finagle.example.java.thriftWithZk;

import com.twitter.finagle.Service;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.LoggerService;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Await;
import com.twitter.util.Function;
import com.twitter.util.Future;

import scala.runtime.BoxedUnit;

public final class ThriftClient {

	private ThriftClient() {
	}

	public static void main(String[] args) throws Exception {
		Service<ThriftClientRequest, byte[]> service = Thrift.client().withSessionPool().maxSize(10).newService(ThriftServer.CONSUMER_PATH);
		LoggerService.ServiceIface client = new LoggerService.ServiceToClient(service);
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
