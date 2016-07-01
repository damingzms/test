package cn.sam.test.spring.lifecycle;

import org.springframework.context.SmartLifecycle;

public class TestSmartLifecycle implements SmartLifecycle {

	@Override
	public void start() {
		System.out.println("SmartLifecycle start ...");
	}

	@Override
	public void stop() {
		System.out.println("SmartLifecycle stop ...");

	}

	@Override
	public boolean isRunning() {
		System.out.println("SmartLifecycle is running ...");
		return false;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		System.out.println("SmartLifecycle stop callback ...");
	}

}
