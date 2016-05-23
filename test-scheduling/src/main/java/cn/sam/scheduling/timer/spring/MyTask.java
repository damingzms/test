package cn.sam.scheduling.timer.spring;

import java.util.TimerTask;

public class MyTask extends TimerTask {

	@Override
	public void run() {
		System.out.println("run task in spring");
	}
	

}
