package cn.sam.concurrency;

public class Robot {
	int i = 0;
	
	public synchronized void makeSounds() {
		System.out.println(Thread.currentThread().getName() + " : in make sounds!");
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " : make sounds!");
		System.out.println(Thread.currentThread().getName() + " : out make sounds!");
	}
	
	public synchronized void doBehaviors() {
		System.out.println(Thread.currentThread().getName() + " : in do some behaviors!");
//		this.notify();
		System.out.println(Thread.currentThread().getName() + " : do some behaviors!");
		System.out.println(Thread.currentThread().getName() + " : out do some behaviors!");
	}

}
