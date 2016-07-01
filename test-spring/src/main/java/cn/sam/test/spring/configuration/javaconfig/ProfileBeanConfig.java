package cn.sam.test.spring.configuration.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cn.sam.test.spring.domain.Orange;

/**
 * 没有使用
 * @author 12984
 *
 */
@Profile("production")
@Configuration
public class ProfileBeanConfig {

	@Bean
	public Orange orange_p() {
		return new Orange();
	}

}
