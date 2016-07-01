package cn.sam.test.spring.configuration.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.sam.test.spring.domain.Orange;

@Configuration
public class ComponentScanBeanConfig {

	@Bean
	public Orange orange() {
		return new Orange();
	}

}
