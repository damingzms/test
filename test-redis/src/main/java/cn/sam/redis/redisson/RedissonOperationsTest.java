package cn.sam.redis.redisson;

import org.reactivestreams.Publisher;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class RedissonOperationsTest {
	
	public static void main(String[] args) {
		testSync();
		
		RedissonUtil.shutdown();
	}

	/**
	 * Sync
	 */
	public static void testSync() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RAtomicLong longObject = redisson.getAtomicLong("myLong");
		longObject.set(2L);

		// sync way
		boolean compareAndSet = longObject.compareAndSet(3, 401);
		System.out.println(compareAndSet);
	}

	/**
	 * Async
	 */
	public static void testAsync() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RAtomicLong longObject = redisson.getAtomicLong("myLong");

		// async way
		RFuture<Boolean> future = longObject.compareAndSetAsync(3, 401);
		future.addListener(new FutureListener<Boolean>() {
			
			@Override
			public void operationComplete(Future<Boolean> future) throws Exception {
				if (future.isSuccess()) {
					// get result
					Boolean result = future.getNow();
					// ...
					System.out.println(result);
				} else {
					// an error has occurred
					Throwable cause = future.cause();
					System.out.println(cause.getMessage());
				}
			}
		});
	}

	/**
	 * Reactive
	 */
	public static void testReactive() {
		RedissonReactiveClient rclient = RedissonUtil.getRedissonReactiveClient();
		RAtomicLongReactive rlongObject = rclient.getAtomicLong("myLong");

		// reactive way
		Publisher<Boolean> compareAndSet = rlongObject.compareAndSet(3, 401);
	}
	
}
