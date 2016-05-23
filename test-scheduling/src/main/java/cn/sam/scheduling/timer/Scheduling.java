package cn.sam.scheduling.timer;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduling {
	
	public static void main(String[] args) {
		TimerTask task1 = new TimerTask() {
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
		TimerTask task2 = new TimerTask() {
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
		Timer timer = new Timer();
//		timer.schedule(task1, 0, 2000);
		timer.scheduleAtFixedRate(task2, 0, 1000);
		System.out.println("exit main");
	}

}
