package cn.sam.scheduling.timer.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SchedulingInSpring {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:cn/sam/scheduling/timer/spring/applicationContext.xml");
	}

}
