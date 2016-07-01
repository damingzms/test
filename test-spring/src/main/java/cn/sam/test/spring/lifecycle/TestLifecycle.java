package cn.sam.test.spring.lifecycle;

import org.springframework.context.Lifecycle;

public class TestLifecycle implements Lifecycle {

	@Override
	public void start() {
		System.out.println("Lifecycle start ...");
	}

	@Override
	public void stop() {
		System.out.println("Lifecycle start ...");

	}

	@Override
	public boolean isRunning() {
		System.out.println("Lifecycle is running ...");
		return false;
	}

}
