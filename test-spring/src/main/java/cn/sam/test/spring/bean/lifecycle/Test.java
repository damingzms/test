package cn.sam.test.spring.bean.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:cn/sam/test/spring/lifecycle/applicationContext.xml");
		((AbstractApplicationContext) context).registerShutdownHook();
	}
}