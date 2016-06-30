package cn.sam.test.spring.configuration.javaconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import cn.sam.test.spring.bean.Apple;

/**
 * <h2>Constraints when authoring {@code @Configuration} classes</h2>
 *
 * <ul>
 * <li>&#064;Configuration classes must be non-final
 * <li>&#064;Configuration classes must be non-local (may not be declared within a method)
 * <li>&#064;Configuration classes must have a default/no-arg constructor and may not use
 * {@link Autowired @Autowired} constructor parameters. Any nested configuration classes
 * must be {@code static}.
 * </ul>
 * 
 * @author 12984
 *
 */
@Configuration
@Import(ComponentScanBeanConfig.class)
@ImportResource("classpath:/cn/sam/test/spring/configuration/javaconfig/applicationContext_is.xml")
@PropertySource("classpath:/cn/sam/test/spring/configuration/javaconfig/config1.properties")
public class BeanConfig {

	@Value("${key1}")
	private String attr1;

	@Value("${key2}")
	private String attr2;

	@Autowired
	Environment env;

	@Bean
	public Apple apple() {
		Apple apple = new Apple();
		apple.setColor(env.getProperty("key1") + attr1);
		apple.setVariety(env.getProperty("key2") + attr2);
		return apple;
	}

	@Bean(name = {"apple_aliase1", "apple_aliase2"})
	public Apple apple_aliase() {
		Apple apple = new Apple();
		return apple;
	}

	@Bean
	@Lazy // @Lazy may be used on @Configuration classes as well. 
	public Apple apple_l() {
		Apple apple = new Apple();
		return apple;
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Apple apple_prototype() {
		Apple apple = new Apple();
		return apple;
	}

	@Bean
	public Apple apple_ref() {
		
		// Eeference other @Bean method. Such so-called 'inter-bean references' are guaranteed to respect scoping and AOP semantics, just like getBean() lookups would.
		Apple apple2 = apple();
		Apple apple = new Apple();
		apple.setColor(apple2.getColor());
		return apple;
	}

	@Configuration
	static class NestedConfig {

		@Bean
		public Apple apple_n() {
			Apple apple = new Apple();
			return apple;
		}
	}
	
	/**
	 * <h3>{@code BeanFactoryPostProcessor}-returning {@code @Bean} methods</h3>
	 * <p>
	 * See end of @{@link Bean} Javadoc for further details
	 * @return
	 */
	@Bean
	public static PropertyPlaceholderConfigurer ppc() {
		// instantiate, configure and return ppc ...
		return null;
	}

	public String getAttr1() {
		return attr1;
	}

	public String getAttr2() {
		return attr2;
	}
}
