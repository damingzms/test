package cn.sam.test.test.spring.boot.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Server {
	
	@Bean
	public TestSpringBootServiceFactory exampleServiceFactory() {
		TestSpringBootServiceFactory exampleServiceFactory = new TestSpringBootServiceFactory();
		exampleServiceFactory.setPort(8080);
		return exampleServiceFactory;
	}
	
}