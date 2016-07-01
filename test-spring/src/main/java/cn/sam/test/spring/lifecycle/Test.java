package cn.sam.test.spring.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 不是太清楚Lifecycle、SmartLifecycle的stop方法怎样使用
 * @author 12984
 *
 */
public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/cn/sam/test/spring/lifecycle/applicationContext.xml");
		TestLifecycle t = context.getBean(TestLifecycle.class);
		System.out.println("TestLifecycle is running ? " + t.isRunning());
		TestSmartLifecycle ts = context.getBean(TestSmartLifecycle.class);
		System.out.println("TestSmartLifecycle is running ? " + ts.isRunning());
		TestSmartLifecycle2 ts2 = context.getBean(TestSmartLifecycle2.class);
		System.out.println("TestSmartLifecycle2 is running ? " + ts2.isRunning());
		
		t.start();
		System.out.println("TestLifecycle is running ? " + t.isRunning());
 	}

}
