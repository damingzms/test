package cn.sam.scheduling.spring;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MySpringTaskAnnotation {
	
	private final SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "0/40 * * * * ?")
	public void run() {
		String date = f.format(new Date());
		System.out.println("run spring task annotation, " + date);
	}
	

}
