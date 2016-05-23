package cn.sam.commontest.naming.javaobj.corba;

import cn.sam.commontest.naming.javaobj.corba.helloapp._HelloStub;

public class HelloServant extends _HelloStub {
	public String sayHello() {
		return "\nHello world !!\n" + new java.util.Date();
	}
}