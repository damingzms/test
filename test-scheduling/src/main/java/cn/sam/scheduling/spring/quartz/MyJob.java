package cn.sam.scheduling.spring.quartz;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyJob {

	public void run() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println("run spring quartz job : " + System.currentTimeMillis());
	}
	
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:cn/sam/scheduling/spring/quartz/applicationContext.xml");
	}

}
