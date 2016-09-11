package cn.sam.redis.redisson;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

public class RedissonDistributedLocksAndSynchronizersTest {
	
	public static void main(String[] args) throws InterruptedException {
		testLock();
		
		RedissonUtil.shutdown();
	}

	/**
	 * Lock -- Redisson distributed reentrant Lock <br>
	 * RLock objects behave according java Lock specification. It means only lock owner thread can unlock it otherwise IllegalMonitorStateException would be thrown.
	 * But if you still need such scenario use RSemaphore object.
	 * 
	 * @throws InterruptedException
	 */
	public static void testLock() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RLock lock = redisson.getLock("anyLock");
		
		// Most familiar locking method
		lock.lock();

		// Lock time-to-live support
		// releases lock automatically after 10 seconds if unlock method not invoked
		lock.lock(10, TimeUnit.SECONDS);

		// Wait for 100 seconds and automatically unlock it after 10 seconds
		boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
		// ...
		lock.unlock();
		
		// Redisson also provides an asynchronous methods for Lock object
		lock.lockAsync();
		lock.lockAsync(10, TimeUnit.SECONDS);
		Future<Boolean> future = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
	}

	/**
	 * Fair Lock -- Redisson distributed reentrant fair Lock <br>
	 * It guarantees that Redisson client threads will acquire it in is same order they requested it. 
	 * 
	 * @throws InterruptedException
	 */
	public static void testFairLock() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RLock fairLock = redisson.getFairLock("anyLock");
		// Most familiar locking method
		fairLock.lock();

		// Lock time-to-live support
		// releases lock automatically after 10 seconds if unlock method not invoked
		fairLock.lock(10, TimeUnit.SECONDS);

		// Wait for 100 seconds and automatically unlock it after 10 seconds
		boolean res = fairLock.tryLock(100, 10, TimeUnit.SECONDS);
		// ...
		fairLock.unlock();
		
		// Redisson also provides an asynchronous methods for fair Lock object
		fairLock.lockAsync();
		fairLock.lockAsync(10, TimeUnit.SECONDS);
		Future<Boolean> future = fairLock.tryLockAsync(100, 10, TimeUnit.SECONDS);
	}

	/**
	 * MultiLock <br>
	 * Each RLock object may belong to different Redisson instances.
	 * 
	 * @throws InterruptedException
	 */
	public static void testMultiLock() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RLock lock1 = redisson.getLock("lock1");
		RLock lock2 = redisson.getLock("lock2");
		RLock lock3 = redisson.getLock("lock3");

		RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
		// locks: lock1 lock2 lock3
		lock.lock();
		// ...
		lock.unlock();
	}

	/**
	 * RedLock <br>
	 * Each RLock object may belong to different Redisson instances.
	 * 
	 * @throws InterruptedException
	 */
	public static void testRedLock() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RLock lock1 = redisson.getLock("lock1");
		RLock lock2 = redisson.getLock("lock2");
		RLock lock3 = redisson.getLock("lock3");

		RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
		// locks: lock1 lock2 lock3
		lock.lock();
		// ...
		lock.unlock();
	}

	/**
	 * ReadWriteLock <br>
	 * 
	 * @throws InterruptedException
	 */
	public static void testReadWriteLock() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RReadWriteLock rwlock = redisson.getReadWriteLock("anyRWLock");
		// Most familiar locking method
		RLock readLock = rwlock.readLock();
		readLock.lock();
		// or
		RLock writeLock = rwlock.writeLock();
		writeLock.lock();

		// Lock time-to-live support
		// releases lock automatically after 10 seconds
		// if unlock method not invoked
		readLock.lock(10, TimeUnit.SECONDS);
		// or
		writeLock.lock(10, TimeUnit.SECONDS);

		// Wait for 100 seconds and automatically unlock it after 10 seconds
		boolean res = readLock.tryLock(100, 10, TimeUnit.SECONDS);
		// or
		boolean res1 = writeLock.tryLock(100, 10, TimeUnit.SECONDS);
		// ...
		
		readLock.unlock();
		// or
		writeLock.unlock();
	}

	/**
	 * Semaphore <br>
	 * 
	 * @throws InterruptedException
	 */
	public static void testSemaphore() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RSemaphore semaphore = redisson.getSemaphore("semaphore");
		semaphore.acquire();
		semaphore.acquireAsync();
		semaphore.acquire(23);
		
		semaphore.tryAcquire();
		semaphore.tryAcquireAsync();
		semaphore.tryAcquire(23, TimeUnit.SECONDS);
		semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
		
		semaphore.release(10);
		semaphore.release();
		semaphore.releaseAsync();
	}

	/**
	 * PermitExpirableSemaphore <br>
	 * Each permit identified by own id and could be released only using its id.
	 * <p>
	 * need a higher version of redisson
	 * 
	 * @throws InterruptedException
	 */
	public static void testPermitExpirableSemaphore() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
//		RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");
//		String permitId = semaphore.acquire();
//		// acquire permit with lease time = 2 seconds
//		String permitId1 = semaphore.acquire(2, TimeUnit.SECONDS);
//		// ...
//		semaphore.release(permitId);
	}

	/**
	 * CountDownLatch <br>
	 * 
	 * @throws InterruptedException
	 */
	public static void testCountDownLatch() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
		latch.trySetCount(1);
		latch.await();

		// in other thread or other JVM
		RCountDownLatch latch1 = redisson.getCountDownLatch("anyCountDownLatch");
		latch1.countDown();
	}

}
