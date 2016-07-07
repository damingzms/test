package cn.sam.commontest.javase;

public class TestDeamon {
	
	public static void main(String[] args) {
		System.out.println("enter main...");
		Thread thread = new Thread() {
			public void run() {
				System.out.println("enter run...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("exit run...");
			};
		};
//		thread.setDaemon(true);
		thread.start();
		System.out.println("exit main...");
//		System.exit(0);
	}

}
