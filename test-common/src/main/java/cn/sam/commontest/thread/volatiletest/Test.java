package cn.sam.commontest.thread.volatiletest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile 关键字只能保证可见性，不能保证原子性
 * 
 * @author JoJo
 *
 */
public class Test {
	public int inc = 0;
	
	public volatile int vinc = 0;

	public AtomicInteger ainc = new AtomicInteger();

	public void increase() {
		inc++;
		vinc++;
		ainc.getAndIncrement();
	}

	public static void main(String[] args) {
		final Test test = new Test();
		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					for (int j = 0; j < 1000; j++)
						test.increase();
				};
			}.start();
		}

		while (Thread.activeCount() > 1)
			// 保证前面的线程都执行完
			Thread.yield();
		System.out.println(test.inc);
		System.out.println(test.vinc);
		System.out.println(test.ainc);
	}
}