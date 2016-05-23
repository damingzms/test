package cn.sam.concurrency;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
		final Robot robot = new Robot();
		Runnable r = new Runnable() {
			public void run() {
				if ("t1".equals(Thread.currentThread().getName())) {
					robot.makeSounds();
				} else {
					robot.doBehaviors();
				}
			}
		};
		Thread t1 = new Thread(r, "t1");
		t1.start();
		Thread t2 = new Thread(r, "t2");
		t2.start();
	}
	
}
