package cn.sam.test.spring.configuration.javaconfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.sam.test.spring.domain.Apple;
import cn.sam.test.spring.domain.Orange;

public class Test {

	public static void main(String[] args) {
		
		// 1. Via AnnotationConfigApplicationContext
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(BeanConfig.class);
		ctx.refresh();
		Apple a = (Apple) ctx.getBean("apple");
		System.out.println(a);
		
		Orange o = (Orange) ctx.getBean("orange"); // 虽然没有调用register方法注册ComponentScanBeanConfig，但是请查看BeanConfig中的@import注解
		System.out.println(o);
		Apple a_is = (Apple) ctx.getBean("apple_is"); // BeanConfig中的@ImportResource注解，导入xml配置
		System.out.println(a_is);
		Apple a_n = (Apple) ctx.getBean("apple_n"); // BeanConfig中的NestedConfig内部类
		System.out.println(a_n);
		Apple a_l = (Apple) ctx.getBean("apple_l"); // BeanConfig中的apple_l方法，延迟加载
		System.out.println(a_l);
		Apple apple_aliase2 = (Apple) ctx.getBean("apple_aliase2"); // BeanConfig中的apple_aliase方法，别名
		System.out.println(apple_aliase2);

		// 2.1-2. Via Spring <beans> XML And Via component scanning
//		ApplicationContext ctx1 = new ClassPathXmlApplicationContext("classpath:cn/sam/test/spring/configuration/javaconfig/applicationContext.xml");
//		Orange o1 = (Orange) ctx1.getBean("orange");
//		System.out.println(o1);
//		Apple a1 = (Apple) ctx1.getBean("apple");
//		
//		// 3. Working with externalized values
//		System.out.println(a1.getColor());
//		System.out.println(a1.getVariety());
		
	}
}