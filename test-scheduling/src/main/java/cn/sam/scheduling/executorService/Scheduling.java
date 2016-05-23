package cn.sam.scheduling.executorService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduling {
	
	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		Thread task1 = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("schedule - run task : " + System.currentTimeMillis());
//				throw new RuntimeException();
			}
		};
		Thread task2 = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("scheduleAtFixedRate - run task : " + System.currentTimeMillis());
			}
		};
//		service.scheduleWithFixedDelay(task1, 0, 1000, TimeUnit.MILLISECONDS);
		service.scheduleAtFixedRate(task2, 0, 1000, TimeUnit.MILLISECONDS);
		System.out.println("exit main");
	}

}
