package cn.sam.scheduling.spring;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class MySpringTaskAnnotationFixedRate {

	@Scheduled(fixedRate = 2000, initialDelay = 5000)
	public void run() throws InterruptedException {
		Thread.sleep(1000);
		System.out.println("run spring task annotation : " + System.currentTimeMillis());
	}
	

}
