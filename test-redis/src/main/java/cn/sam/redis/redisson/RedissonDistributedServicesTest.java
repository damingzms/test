package cn.sam.redis.redisson;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.redisson.CronSchedule;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RExecutorService;
import org.redisson.api.RFuture;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RMap;
import org.redisson.api.RRemoteService;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
import org.redisson.api.RedissonClient;
import org.redisson.api.RemoteInvocationOptions;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;
import org.redisson.api.annotation.RInject;
import org.redisson.api.annotation.RRemoteAsync;

import io.netty.util.concurrent.FutureListener;

/**
 * 1.Remote service
 * 2.Live Object service
 * 3.Distributed executor service
 * 4.Distributed scheduled executor service
 *
 */
public class RedissonDistributedServicesTest {
	
	public static void main(String[] args) {
		try {
//				testRemoteRervice();
//				testLiveObjectService();
				testDistributedExecutorService();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedissonUtil.shutdown();
		}
	}
	
/* 1.Remote service Start */
	
	/**
	 * Remote service <br>
	 * Client and server side instances shall be using the same remote interface and backed by redisson instances created using the same server connection configuration. 
	 * Client and server side instances could be run in same JVM.
	 * <p>
	 * Message flow: <br>
	 * RemoteService creates two queues per invocation. 
	 * One queue for request (being listened by server side instance) and another one is for ack-response and result-response (being listened by client side instance). 
	 * Ack-response used to determine if method executor has got a request. If it doesn't during ack timeout then RemoteServiceAckTimeoutException will be thrown.
	 * 
	 */
	public static void testRemoteRervice() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		
		// 1.Server side instance - executes remote method (worker instance). Example:
		RRemoteService remoteService = redisson.getRemoteSerivce();
		SomeServiceImpl someServiceImpl = new SomeServiceImpl();

		// register remote service before any remote invocation
		// can handle only 1 invocation concurrently
		remoteService.register(SomeServiceInterface.class, someServiceImpl);

		// register remote service able to handle up to 12 invocations concurrently.
		// invocations exceeding this number will be queued for the next available executor.
		remoteService.register(SomeServiceInterface.class, someServiceImpl, 12);

		// register remote service using separate 
		// ExecutorService used to execute remote invocation
		ExecutorService executor = Executors.newFixedThreadPool(5);
		remoteService.register(SomeServiceInterface.class, someServiceImpl, 5, executor);
		
		// 2.Client side instance - invokes remote method. Example:
		RRemoteService remoteServicec = redisson.getRemoteSerivce();
		SomeServiceInterface service = remoteServicec.get(SomeServiceInterface.class);
		String result = service.doSomeStuff(1L, "secondParam", "otherParam");
		
		// 3.invokes remote method using options
		// 1 second ack timeout and 30 seconds execution timeout
		RemoteInvocationOptions options = RemoteInvocationOptions.defaults();

		// no ack but 30 seconds execution timeout
		RemoteInvocationOptions options1 = RemoteInvocationOptions.defaults().noAck();

		// 1 second ack timeout then forget the result
		RemoteInvocationOptions options2 = RemoteInvocationOptions.defaults().noResult();

		// 1 minute ack timeout then forget about the result
		RemoteInvocationOptions options3 = RemoteInvocationOptions.defaults().expectAckWithin(1, TimeUnit.MINUTES).noResult();

		// no ack and forget about the result (fire and forget)
		RemoteInvocationOptions options4 = RemoteInvocationOptions.defaults().noAck().noResult();

		SomeServiceInterface serviceo = remoteServicec.get(SomeServiceInterface.class, options);
		
		// 4.invokes remote method using Asynchronous calls
		SomeServiceInterfaceAsync asyncService = remoteServicec.get(SomeServiceInterfaceAsync.class);
		RFuture<String> future = asyncService.doSomeStuff(1L, "secondParam", "otherParam");
		
		// 5.cancel invocation
		// Remote service offers ability to cancel invocation in any stages of its execution.
		future.cancel(true);
		
	}
	
	public static interface SomeServiceInterface {
		String doSomeStuff(long l, String... p);
	}
	
	public static class SomeServiceImpl implements SomeServiceInterface {

		@Override
		public String doSomeStuff(long l, String... params) {
			StringBuilder sb = new StringBuilder();
			sb.append(l).append(" - ");
			for (String p : params) {
				sb.append(p).append("-");
			}
			return sb.toString();
		}
		
	}
	
	// async interface for SomeServiceInterface, defined in client
	@RRemoteAsync(SomeServiceInterface.class)
	public interface SomeServiceInterfaceAsync {

	    RFuture<String> doSomeStuff(long l, String... p);

	}
	
/* 1.Remote service End */

/* 2.Live Object service Start */

	/**
	 * Live Object service <br>
	 * A Live Object can be understood as an enhanced version of standard Java object,
	 * of which an instance reference can be shared not only between threads in a single JVM, but can also be shared between different JVMs across different machines.
	 * <p>
	 * Redisson Live Object (RLO) realised this idea by mapping all the fields inside a Java class to a redis hash through a runtime-constructed proxy class.
	 * All the get/set methods of each field are translated to hget/hset commands operated on the redis hash.
	 * <p>
	 * Since the redis server is a single-threaded application, all field access to the live object is automatically executed in atomic fashion:
	 * a value will not be changed when you are reading it.
	 * <p>
	 * With RLO, you can treat the redis server as a shared Heap space for all connected JVMs.
	 */
	public static void testLiveObjectService() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RLiveObjectService service = redisson.getLiveObjectService();
		
		// 1.
		
		//Standard Java object instance
		MyObject standardObject1 = new MyObject();
		standardObject1.setName("standard1");

		//Of course you can use non-default constructor
		MyObject standardObject2 = new MyObject("standard2");
		
		//instantiate the object with the service
		MyObject liveObject1 = service.<MyObject, String>getOrCreate(MyObject.class, "liveObjectId");
		//Behind scense, it tries to locate the constructor with one argument and invoke with the id value, 
		//"liveObjectId" in this case. If the constructor is not found, falls back on default constructor
		//and then call setName("liveObjectId") before returns back to you. 
		
		//Setting the "value" field is the same
		standardObject1.setValue("abc");//the value "abc" is stored in heapspace in VM

		standardObject2.setValue("abc");//same as above

		liveObject1.setValue("abc");
		//the value "abc" is stored inside redis, no value is stored in heap. (OK, there
		//is a string pool, but the value is not referenced here in the object, so it can
		//be garbage collected.)

		//Getting the "value" out is just the same
		System.out.println(standardObject1.getValue());
		//It should give you "abc" in the console, the value is retrieved from heapspace in the VM;

		System.out.println(standardObject2.getValue());//same as above.

		System.out.println(liveObject1.getValue());
		//output is the same as above, but the value is retrieved from redis.
		
		// 2.
		
		// In the following case, the type of the "value" field is a mutable type.
		// In a standard Java object, when you invoke the getValue() method, the reference to this MyOtherObject instance is returned to you.
		// When you invoke the same method on a RLO, a reference of a new instance is returned.
		
		//Redisson Live Object behaviour:
		MyLiveObject myLiveObject = service.getOrCreate(MyLiveObject.class, "1");
		myLiveObject.setValue(new MyOtherObject());
		System.out.println(myLiveObject.getValue() == myLiveObject.getValue());
		//False (unless you use a custom Codec with object pooling)

		//Standard Java Object behaviour:
		MyLiveObject notLiveObject = new MyLiveObject();
		notLiveObject.setValue(new MyOtherObject());
		System.out.println(notLiveObject.getValue() == notLiveObject.getValue());
		//True
		
		//Redisson Live Object behaviour:
		MyLiveObject myLiveObjectR = service.getOrCreate(MyLiveObject.class, "1");
		MyOtherObject otherR = new MyOtherObject();
		otherR.setOtherName("ABC");
		myLiveObjectR.setValue(otherR);
		System.out.println(myLiveObjectR.getValue().getOtherName());
		//ABC

		otherR.setOtherName("BCD");
		System.out.println(myLiveObjectR.getValue().getOtherName());
		//still ABC

		myLiveObjectR.setValue(otherR);
		System.out.println(myLiveObjectR.getValue().getOtherName());
		//now it's BCD

		//Standard Java Object behaviour:
		MyLiveObject myLiveObjectS = new MyLiveObject();
		MyOtherObject otherS = new MyOtherObject();
		otherS.setOtherName("ABC");
		myLiveObjectS.setValue(otherS);
		System.out.println(myLiveObjectS.getValue().getOtherName());
		//ABC

		otherS.setOtherName("BCD");
		System.out.println(myLiveObjectS.getValue().getOtherName());
		//already is BCD

		myLiveObjectS.setValue(otherS);
		System.out.println(myLiveObjectS.getValue().getOtherName());
		//still is BCD
		
		// The reason for this difference in behaviour is because we are not keeping any of the object states, 
		// and each setter and getter call will serialize and deserialize the value to and from redis back to local VM. 
		
		// If you prefer to stick to standard Java behaviour, you can always convert the MyOtherObject into a Redisson Live Object.
		
		//A Redisson Live Object with nested Redisson Live Object behaviour:
		MyLiveObject myLiveObject1 = service.getOrCreate(MyLiveObject.class, "1");
		MyOtherLiveObject other1 = service.getOrCreate(MyOtherLiveObject.class, "2");
		other1.setOtherName("ABC");
		myLiveObject1.setValue1(other1);
		System.out.println(myLiveObject1.getValue1().getOtherName());
		//ABC

		other1.setOtherName("BCD");
		System.out.println(myLiveObject1.getValue1().getOtherName());
		//you see, already is BCD
		// 但是实测证明，还是ABC

		myLiveObject1.setValue1(other1);
		System.out.println(myLiveObject1.getValue1().getOtherName());
		//and again still is BCD
		
		// 3. Advanced Usage
		
		// As described before, RLO classes are proxy classes which can be fabricated when needed and then get cached in a RedissonClient instance against its original class.
		// This process can be a bit slow,
		// and it is recommended to pre-register all the Redisson Live Object classes via RedissonLiveObjectService for any kind of delay-sensitive applications.
		service.registerClass(MyObject.class);
		service.unregisterClass(MyObject.class);
		Boolean registered = service.isClassRegistered(MyObject.class);
		
	}
	
	/**
	 * At the moment, RLO can only be classes with the default constructor or classes with a single-argument constructor, 
	 * and the argument is assumed to be used as the value for field with RId annotaion. 
	 * As mentioned above, the type of the RId field cannot be an Array type.
	 */
	@REntity
	public static class MyObject {
		
		// @RId - The field with this annotation is the only field that has its value also kept in the local VM. You can only have one RId annotation per class.
	    @RId
	    private String name;
	    private String value;

	    public MyObject(String name) {
	        this.name = name;
	    }

	    public MyObject() {
	    }

	    public String getName() {
	        return name;
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }
	}
	
	@REntity
	public static class MyLiveObject {
	    @RId
	    private String name;
	    private MyOtherObject value;
	    private MyOtherLiveObject value1;

	    public MyLiveObject(String name) {
	        this.name = name;
	    }

	    public MyLiveObject() {
	    }

	    public String getName() {
	        return name;
	    }

	    public MyOtherObject getValue() {
	        return value;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public void setValue(MyOtherObject value) {
	        this.value = value;
	    }

		public MyOtherLiveObject getValue1() {
			return value1;
		}

		public void setValue1(MyOtherLiveObject value1) {
			this.value1 = value1;
		}
	    
	}
	
	public static class MyOtherObject{
		
	    private String otherName;

		public String getOtherName() {
			return otherName;
		}

		public void setOtherName(String otherName) {
			this.otherName = otherName;
		}
	    
	}
	
	@REntity
	public static class MyOtherLiveObject{
		
		@RId
	    private String otherName;

		public String getOtherName() {
			return otherName;
		}

		public void setOtherName(String otherName) {
			this.otherName = otherName;
		}
	    
	}
	
/* 2.Live Object service End */

/* 3.Distributed executor service Start */

	/**
	 * Distributed executor service <br>
	 * <p>
	 * Redisson node don't need to have task classes in classpath. They are loaded automatically by Redisson node ClassLoader. 
	 * Thus Redisson node reloading is not needed for each new task class.
	 * 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 * 
	 */
	public static void testDistributedExecutorService() throws InterruptedException, ExecutionException, TimeoutException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		
		// Tasks
		RExecutorService executorService = redisson.getExecutorService("myExecutor");
		Future<Long> future = executorService.submit(new CallableTask());
		Long result = future.get(5, TimeUnit.SECONDS);	// TimeoutException
		System.out.println(result);
		
		executorService.submit(new RunnableTask(123));
		
		// Extended asynchronous mode
		RFuture<Long> submitAsync = executorService.submitAsync(new CallableTask());
		submitAsync.addListener(new FutureListener<Long>() {

			@Override
			public void operationComplete(io.netty.util.concurrent.Future<Long> future) throws Exception {
				// ...
			}
		});
		
		// Task execution cancellation
		Future<Long> future1 = executorService.submit(new CallableTask4Cancel());
		RFuture<Long> future2 = executorService.submitAsync(new CallableTask4Cancel());
		future1.cancel(true);
	}
	
	public static class CallableTask implements Callable<Long> {

	    @RInject
	    private RedissonClient redissonClient;

	    @Override
	    public Long call() throws Exception {
	        RMap<String, Integer> map = redissonClient.getMap("myMap");
	        map.put("k1", 3);
	        map.put("k2", 5);
	        Long result = 0L;
	        for (Integer value : map.values()) {
	            result += value;
	        }
	        return result;
	    }

	}
	
	public static class RunnableTask implements Runnable {

	    @RInject
	    private RedissonClient redissonClient;

	    private long value;

	    public RunnableTask() {
	    }

	    public RunnableTask(long value) {
	        this.value = value;
	    }

	    @Override
	    public void run() {
	        RAtomicLong atomic = redissonClient.getAtomicLong("myAtomic");
	        long l = atomic.addAndGet(value);
	        System.out.println(l);
	    }

	}
	
	public static class CallableTask4Cancel implements Callable<Long> {

	    @RInject
	    private RedissonClient redissonClient;

	    @Override
	    public Long call() throws Exception {
	        RMap<String, Integer> map = redissonClient.getMap("myMap");
	        Long result = 0L;
	        // map contains many entries
	        for (Integer value : map.values()) {           
	           if (Thread.currentThread().isInterrupted()) {
	                // task has been canceled
	                return null;
	           }
	           result += value;
	        }
	        return result;
	    }

	}

/* 3.Distributed executor service End */

/* 4.Distributed scheduled executor service Start */

	/**
	 * Distributed scheduled executor service <br>
	 * Redisson node run jobs from Redis queue.
	 * <p>
	 * Redisson node don't need to have task classes in classpath. They are loaded automatically by Redisson node ClassLoader.
	 * Thus Redisson node reloading is not needed for each new task class.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	public static void testDistributedScheduledExecutorService() throws InterruptedException, ExecutionException, TimeoutException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		
		// Scheduling a task
		RScheduledExecutorService executorService = redisson.getExecutorService("myExecutor");
		ScheduledFuture<Long> future = executorService.schedule(new CallableTask(), 1, TimeUnit.MINUTES);
		Long result = future.get(2, TimeUnit.MINUTES);
		
		ScheduledFuture<?> future1 = executorService.schedule(new RunnableTask(123), 10, TimeUnit.HOURS);
		ScheduledFuture<?> future2 = executorService.scheduleAtFixedRate(new RunnableTask(123), 10, 25, TimeUnit.HOURS);
		ScheduledFuture<?> future3 = executorService.scheduleWithFixedDelay(new RunnableTask(123), 5, 10, TimeUnit.HOURS);
		
		// Scheduling a task with cron expression
		executorService.schedule(new RunnableTask(), CronSchedule.of("10 0/5 * * * ?"));
		executorService.schedule(new RunnableTask(), CronSchedule.dailyAtHourAndMinute(10, 5));
		executorService.schedule(new RunnableTask(), CronSchedule.weeklyOnDayAndHourAndMinute(12, 4, Calendar.MONDAY, Calendar.FRIDAY));
		
		// Task scheduling cancellation
		RScheduledFuture<?> scheduleAsync = executorService.scheduleAsync(new CallableTask4Cancel(), 1, TimeUnit.HOURS);
		scheduleAsync.cancel(true);
		String taskId = scheduleAsync.getTaskId();
		executorService.cancelScheduledTask(taskId);
	}
	
	
/* 4.Distributed scheduled executor service End */
	
}
