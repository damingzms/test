package com.twitter.finagle.example.scala.thrift_deprecated;

//import com.twitter.finagle.ListeningServer;
//import com.twitter.finagle.Thrift;
//import com.twitter.finagle.example.thriftscala.Hello;
//import com.twitter.util.Await;
//import com.twitter.util.Future;
//import com.twitter.util.TimeoutException;

/**
 * 需要设置scrooge-maven-plugin插件，根据IDL（*.thrift）生成scala代码。见pom.xml文件
 * 
 * 此实现使用了deprecated api，代码可以正常运行，为了避免对java版本的代码造成影响，将其注释掉
 * 
 */
public final class ThriftServer {

	private ThriftServer() {
	}

//	public static class HelloImpl implements Hello.FutureIface {
//		public Future<String> hi() {
//			return Future.value("hi");
//		}
//	}
//
//	public static void main(String[] args) throws TimeoutException, InterruptedException {
//		Hello.FutureIface impl = new HelloImpl();
//		ListeningServer server = Thrift.server().serveIface("localhost:8080", impl);
//		Await.ready(server);
//	}
}
