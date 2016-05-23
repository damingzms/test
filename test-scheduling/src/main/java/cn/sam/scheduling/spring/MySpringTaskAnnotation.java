package cn.sam.scheduling.spring;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class MySpringTaskAnnotation {

	@Scheduled(cron = "0/2 * * * * ?")
	public void run() {
		System.out.println("run spring task annotation");
	}
	

}
