package cn.sam.test.finagle.thrift_zk;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.LoggerService;
import com.twitter.finagle.example.thriftjava.ReadException;
import com.twitter.finagle.example.thriftjava.TLogObjRequest;
import com.twitter.finagle.example.thriftjava.TLogObjResponse;
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
 * filters等的用法：/test-finagle/src/main/scala/com/twitter/finagle/example/thrift/ThriftServiceIfaceExample.scala
 * 
 * zookeeper集成：/test-finagle/src/main/scala/com/twitter/finagle/example/zookeeper
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
			return Future.value(String.format("You've sent: ('%s', %s)" , logLevel, message));
		}

		@Override
		public Future<TLogObjResponse> logObj(TLogObjRequest request) {
			System.out.println(String.format("[%s] Server received message: '%s'", request.getLogLevel(), request.getMessage()));
			TLogObjResponse response = new TLogObjResponse();
			response.setRespCode(200);
			response.setRespDesc(String.format("Success! You've sent: ('%s', %s)", request.getLogLevel(), request.getMessage()));
			return Future.value(response);
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

	public static void main(String[] args) throws TimeoutException, InterruptedException, UnknownHostException {

		// 1.初始化service
		LoggerService.ServiceIface impl = new LoggerServiceImpl();
		LoggerService.Service service = new LoggerService.Service(impl);

		// 3.Server and Announcing it in zookeeper
		InetSocketAddress addr = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 8081);
		ListeningServer server = Thrift.server()
				.withLabel("finagle server")
				.withAdmissionControl().concurrencyLimit(1, 0)
				.withSession().maxIdleTime(Duration.fromMilliseconds(10000L))
//				.withSession().maxLifeTime(timeout)
//				.withMonitor(null)
//				.withTracer(tracer)
				.serveAndAnnounce(PROVIDER_PATH, addr, service);
		Await.ready(server);
		
//		ServerBuilder<Nothing$, Nothing$, Nothing$, Yes, Yes> bindTo = ServerBuilder.get()
//		.name("finagle server").keepAlive(true)
//		.hostConnectionMaxIdleTime(Duration.fromMilliseconds(10000L))
//		.readTimeout(Duration.fromMilliseconds(10000L))
//		//.tracer(ZipkinTracer.mk(scribeIP, scribePort,sr,1))
//		.requestTimeout(Duration.fromMilliseconds(10000L))
//		.bindTo(new InetSocketAddress(8080));
	}
	
}
