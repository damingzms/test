package cn.sam.scheduling.spring;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class MySpringTaskAnnotationFixedDelay {

	@Scheduled(fixedDelay = 2000)
	public void run() throws InterruptedException {
		Thread.sleep(1000);
		System.out.println("run spring task annotation : " + System.currentTimeMillis());
	}
	

}
