package cn.sam.test.rmi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringRmi {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//		context.getBean("serviceExporter");
	}

}
