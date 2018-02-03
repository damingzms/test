package cn.sam.test.springcloud.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Server {
	
	@Bean
	public ExampleServiceFactory exampleServiceFactory() {
		ExampleServiceFactory exampleServiceFactory = new ExampleServiceFactory();
		exampleServiceFactory.setPort(8080);
		return exampleServiceFactory;
	}
	
}