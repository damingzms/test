package com.twitter.finagle.example.java.thriftWithZk;

import java.net.InetSocketAddress;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.LoggerService;
import com.twitter.finagle.example.thriftjava.ReadException;
import com.twitter.util.Await;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import com.twitter.util.TimeoutException;

/**
 * 需要设置scrooge-maven-plugin插件，根据IDL（*.thrift）生成java代码。见pom.xml文件
 * <p>
 * Scrooge is meant to be a replacement for the Apache Thrift code generator, and generates conforming, binary-compatible codecs by building on top of libthrift.<br>
 * 为Scrooge设置--finagle参数，可以根据IDL生成finagle的thrift协议的代码。见pom.xml文件
 * <p>
 * Scrooge对required/optional/unspecified-requiredness and default values的具体处理：https://twitter.github.io/scrooge/Semantics.html
 * <p>
 * IDL（*.thrift）文件规范，以及使用Scrooge thrift linter工具检查文件内容是否符合规范：https://twitter.github.io/scrooge/Linter.html
 * <p>
 * Scrooge与Finagle的整合：https://twitter.github.io/finagle/guide/Protocols.html#using-finagle-thrift
 * 
 */
/**
 * 
 * filters等的用法：/test-finagle/src/main/scala/com/twitter/finagle/example/thrift/ThriftServiceIfaceExample.scala
 * 
 * zookeeper集成：/test-finagle/src/main/scala/com/twitter/finagle/example/zookeeper
 * 
 * TODO 用的依然还是旧的api，参考以上zookeeper集成，尝试使用非deprecated的api，然后学习使用filter等特性
 *
 */
public final class ThriftServer {

	public static final String ZOOKEEPER_DEST = "test-zookeeper01.biostime.it:2181,test-zookeeper02.biostime.it:2181,test-zookeeper03.biostime.it:2181";
	
	public static final String SERVICE_PATH = "/finagle-test/services/echo";
	
    //e.g.:            zk!test-zookeeper01.biostime.it:2181,test-zookeeper02.biostime.it:2181,test-zookeeper03.biostime.it:2181!/finagle-test/services/echo/!0
    //syntax:          schema!host!path!shardId
    //schema:          for server, use zk, for client, use zk2
    //host:            zookeeper connection string
    //path:            service registration path in zookeeper
    //shardId:         it's used internally by Twitter, can be set to 0 in most cases
	public static final String PROVIDER_PATH = String.format("zk!%s!%s!0", ZOOKEEPER_DEST, SERVICE_PATH);
	
	public static final String CONSUMER_PATH = String.format("zk2!%s!%s", ZOOKEEPER_DEST, SERVICE_PATH);

	private ThriftServer() {
	}

	public static class LoggerServiceImpl implements LoggerService.ServiceIface {

		int counter = 0;
		
		@Override
		public Future<String> log(String message, int logLevel) {
			System.out.println(String.format("[%s] Server received: '%s'", logLevel, message));
			return Future.value(String.format("You've sent: ('%s', %s)", message, logLevel));
		}

		@Override
		public Future<Integer> getLogSize() {
			Future<Integer> result = null;
			counter++;
			if (counter % 2 == 1) {
				System.out.println("Server: getLogSize ReadException");
				result = Future.exception(new ReadException());
			} else {
				System.out.println("Server: getLogSize Success");
				result = Future.value(4);
			}
			return result;
		}
	}

	public static void main(String[] args) throws TimeoutException, InterruptedException {
		LoggerService.ServiceIface impl = new LoggerServiceImpl();
		ListeningServer server = Thrift.server().serveAndAnnounce(PROVIDER_PATH, new InetSocketAddress(8080), new LoggerService.Service(impl));
		Await.ready(server);
		
		Thrift.server().withLabel("finagle server").with
		maxConcurrentRequests(maxConcurrentRequests).keepAlive(true)
		.hostConnectionMaxIdleTime(Duration.fromMilliseconds(hostConnectionMaxIdleTime))
		.readTimeout(Duration.fromMilliseconds(readTimeout))
		//.tracer(ZipkinTracer.mk(scribeIP, scribePort,sr,1))
		.requestTimeout(Duration.fromMilliseconds(requestTimeout))
	}
}
