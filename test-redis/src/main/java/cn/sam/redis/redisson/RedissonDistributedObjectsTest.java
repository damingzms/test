package cn.sam.redis.redisson;

import java.util.List;
import java.util.Map;

import org.redisson.api.GeoEntry;
import org.redisson.api.GeoPosition;
import org.redisson.api.GeoUnit;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBitSet;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RGeo;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.protocol.pubsub.Message;
import org.redisson.client.protocol.pubsub.PubSubMessage;
import org.springframework.util.StopWatch;

public class RedissonDistributedObjectsTest {
	
	public static void main(String[] args) {
		testObjectHolder();
		
		RedissonUtil.shutdown();
	}
	
	/**
	 * Each Redisson object bound to Redis key which means object name and can be read via getName method.
	 */
	public static void testName() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RMap map = redisson.getMap("mymap");
		map.getName(); // = mymap
	}
	
	/**
	 * Keys
	 */
	public static void testKeys() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RKeys keys = redisson.getKeys();

		Iterable<String> allKeys = keys.getKeys();
		Iterable<String> foundedKeys = keys.getKeysByPattern("key*");
		long numOfDeletedKeys = keys.delete("obj1", "obj2", "obj3");
		long deletedKeysAmount = keys.deleteByPattern("test?");
		String randomKey = keys.randomKey();
		long keysAmount = keys.count();
	}
	
	/**
	 * Redisson distributed RBucket object for Java is an universal holder for any type of object.
	 */
	public static void testObjectHolder() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBucket<Object> bucket = redisson.getBucket("anyObject");
		bucket.set(new Object());
		Object obj = bucket.get();

		bucket.trySet(new Object());
		bucket.compareAndSet(new Object(), new Object());
		bucket.getAndSet(new Object());
	}
	
	/**
	 * Geospatial holder
	 */
	public static void testGeospatialHolder() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RGeo<String> geo = redisson.getGeo("test");
		geo.add(new GeoEntry(13.361389, 38.115556, "Palermo"), 
		        new GeoEntry(15.087269, 37.502669, "Catania"));
		geo.addAsync(37.618423, 55.751244, "Moscow");

		Double distance = geo.dist("Palermo", "Catania", GeoUnit.METERS);
		RFuture<Map<String, String>> hashAsync = geo.hashAsync("Palermo", "Catania");
		Map<String, GeoPosition> positions = geo.pos("test2", "Palermo", "test3", "Catania", "test1");
		List<String> cities = geo.radius(15, 37, 200, GeoUnit.KILOMETERS);
		Map<String, GeoPosition> citiesWithPositions = geo.radiusWithPosition(15, 37, 200, GeoUnit.KILOMETERS);
	}
	
	/**
	 * Redisson distributed BitSet object for Java has structure similar to java.util.BitSet and represents vector of bits that grows as needed. BitSet size limited by Redis to 4 294 967 295.
	 */
	public static void testBitSet() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBitSet set = redisson.getBitSet("simpleBitset");
		set.set(0, true);
		set.set(1812, false);
		set.clear(0);
		set.xor("anotherBitset");
	}
	
	/**
	 * Redisson distributed AtomicLong object for Java has structure similar to java.util.concurrent.atomic.AtomicLong object.
	 * 
	 * 根据计时结果可看出，init操作是非常耗时的，set操作比其他操作更耗时，另外三个操作效率很高
	 */
	public static void testAtomicLong() {
        StopWatch watch = new StopWatch();
        watch.start();
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		watch.stop();
		System.out.println("init: " + watch.getLastTaskTimeMillis());
		
        watch.start();
		RAtomicLong atomicLong = redisson.getAtomicLong("myAtomicLong");
		watch.stop();
		System.out.println("new: " + watch.getLastTaskTimeMillis());

        watch.start();
		atomicLong.set(3);
		watch.stop();
		System.out.println("set: " + watch.getLastTaskTimeMillis());

        watch.start();
		long l1 = atomicLong.incrementAndGet();
		watch.stop();
		System.out.println("change and get: " + watch.getLastTaskTimeMillis());

        watch.start();
		long l2 = atomicLong.get();
		watch.stop();
		System.out.println("get: " + watch.getLastTaskTimeMillis());
		
		System.out.println(l1);
		System.out.println(l2);
	}
	
	/**
	 * AtomicDouble
	 */
	public static void testAtomicDouble() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RAtomicDouble atomicDouble = redisson.getAtomicDouble("myAtomicDouble");
		atomicDouble.set(2.81);
		atomicDouble.addAndGet(4.11);
		atomicDouble.get();
	}
	
	/**
	 * Topic
	 * Topic listeners will be resubscribed automatically during any Redis connection reconnection or Redis server failover.
	 */
	public static void testTopic() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RTopic<String> topic = redisson.getTopic("anyTopic");
		topic.addListener(new MessageListener<String>() {
		    @Override
		    public void onMessage(String channel, String message) {
		        System.out.println(channel);
		        System.out.println(message);
		    }
		});

		// in other thread or JVM
		RTopic<String> topicPB = redisson.getTopic("anyTopic");
		long clientsReceivedMessage = topicPB.publish("tetetest");
		System.out.println(clientsReceivedMessage);
	}
	
	/**
	 * Pattern Topic
	 */
	public static void testPatternTopic() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		
		// subscribe to all topics by `anyTopic.*` pattern
		RPatternTopic<Message> patternTopic = redisson.getPatternTopic("anyTopic.*");
		int listenerId = patternTopic.addListener(new PatternMessageListener<Message>() {
		    @Override
		    public void onMessage(String pattern, String channel, Message msg) {
		    	System.out.println(pattern);
		    	System.out.println(channel);
		    	System.out.println(msg);
		    }
		});

		// in other thread or JVM
//		RTopic<String> topicPB = redisson.getTopic("anyTopic.11");
//		long clientsReceivedMessage = topicPB.publish("tetetest");
		RTopic<Message> topicPB = redisson.getTopic("anyTopic.11");
		long clientsReceivedMessage = topicPB.publish(new PubSubMessage("", "tetetest"));	// exception
		System.out.println(clientsReceivedMessage);
	}
	
	/**
	 * Bloom filter
	 * 它具有很好的空间和时间效率，用来检测一个元素是不是集合中的一个成员。如果检测结果为是，该元素“不一定”在集合中；但如果检测结果为否，该元素“一定”不在集合中。
	 */
	public static void testBloomFilter() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBloomFilter<String> bloomFilter = redisson.getBloomFilter("sample");
		// initialize bloom filter with 
		// expectedInsertions = 55000000
		// falseProbability = 0.03
		bloomFilter.tryInit(55000000L, 0.03);
		bloomFilter.add("field1Value");
		bloomFilter.add("field8Value");
		boolean contains = bloomFilter.contains("field5Value");
		boolean contains1 = bloomFilter.contains("field8Value");
		System.out.println(contains);
		System.out.println(contains1);
	}
	
	/**
	 * HyperLogLog<br>
	 * HyperLogLog 可以接受多个元素作为输入，并给出输入元素的基数估算值：<br>
		• 基数：集合中不同元素的数量。比如 {'apple', 'banana', 'cherry', 'banana', 'apple'} 的基数就是 3 。<br>
		• 估算值：算法给出的基数并不是精确的，可能会比实际稍微多一些或者稍微少一些，但会控制在合理的范围之内。<br>
		HyperLogLog 的优点是，即使输入元素的数量或者体积非常非常大，计算基数所需的空间总是固定的、并且是很小的。<br>
		在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。<br>
		但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以HyperLogLog 不能像集合那样，返回输入的各个元素。<br>
		<p>
		好像元素个数较小时，不是很精确，如：1、2、1，输出是3
	 */
	public static void testHyperLogLog() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RHyperLogLog<Integer> log = redisson.getHyperLogLog("log");
		log.add(1);
		log.add(2);
		log.add(1);
		log.add(1);
		log.add(1);
		log.add(10);
		log.add(1);
		log.add(1);
		log.add(1);
		log.add(3);
		log.add(3);
		log.add(1);
		log.add(1);

		long count = log.count();
		System.out.println(count);
	}
	
}
