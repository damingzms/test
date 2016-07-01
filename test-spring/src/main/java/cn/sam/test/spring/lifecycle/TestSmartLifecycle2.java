package cn.sam.test.spring.lifecycle;

import org.springframework.context.SmartLifecycle;

public class TestSmartLifecycle2 implements SmartLifecycle {

	@Override
	public void start() {
		System.out.println("SmartLifecycle2 start ...");
	}

	@Override
	public void stop() {
		System.out.println("SmartLifecycle2 stop ...");

	}

	@Override
	public boolean isRunning() {
		System.out.println("SmartLifecycle2 is running ...");
		return true;
	}

	@Override
	public int getPhase() {
		return 2;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		System.out.println("SmartLifecycle2 stop callback ...");
	}

}
