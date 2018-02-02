package cn.sam.test.springcloud.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Server {
	
	@Bean
	public ServiceFactory serviceFactory() {
		ServiceFactory serviceFactory = new ServiceFactory();
		serviceFactory.setPort(8080);
		return serviceFactory;
	}
	
}