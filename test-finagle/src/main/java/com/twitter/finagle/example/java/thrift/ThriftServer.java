package com.twitter.finagle.example.java.thrift;

import java.net.InetSocketAddress;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.Hello;
//import com.twitter.finagle.example.thriftjava.Hello;
import com.twitter.util.Await;
import com.twitter.util.TimeoutException;

public final class ThriftServer {

  private ThriftServer() { }

  /**
   * Runs the example with given {@code args}.
   *
   * @param args the argument list
   */
  public static void main(String[] args) throws TimeoutException, InterruptedException {
    //#thriftserverapi
    Hello.ServiceIface impl = new HelloImpl();
    ListeningServer server = Thrift.server().serveIface(new InetSocketAddress(8080), impl);
    Await.ready(server);
    //#thriftserverapi
  }
}
