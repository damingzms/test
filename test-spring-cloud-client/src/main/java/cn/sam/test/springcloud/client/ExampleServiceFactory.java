package cn.sam.test.springcloud.client;

import org.springframework.beans.factory.annotation.Autowired;

import cn.sam.test.springcloud.client.service.Example;

public class ExampleServiceFactory implements ServiceFactory {

    private String protocol = "http";

    private String host = "127.0.0.1";

    private int port = -1;

    private String basePath = "";
    
    @Autowired
    private Example example;
    
	public Example getExample() {
		return example;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

}
