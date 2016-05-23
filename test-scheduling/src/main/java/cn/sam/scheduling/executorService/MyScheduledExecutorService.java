package cn.sam.scheduling.executorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MyScheduledExecutorService {
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		// *1
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		// *2
		Runnable task1 = new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Taskrepeating : " + System.currentTimeMillis());
			}
		};
		// *3
		final ScheduledFuture<?> future1 = service.scheduleWithFixedDelay(task1, 0,
				2, TimeUnit.SECONDS);
//		int count = 0;
//		while(!future1.isDone()) {
//			count++;
//			Thread.sleep(1000);
//			System.out.println("main method is waiting future1 to be done!");
//			if (count >= 10) {
//				future1.cancel(false);
//			}
//		}
		// *4
		ScheduledFuture<String> future2 = service.schedule(new Callable<String>() {
			public String call() {
				future1.cancel(false);
				return "taskcancelled!";
			}
		}, 10, TimeUnit.SECONDS);
		System.out.println(future2.get());
		// *5
		service.shutdown();
	}

}
