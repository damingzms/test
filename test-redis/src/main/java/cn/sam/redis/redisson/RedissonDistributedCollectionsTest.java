package cn.sam.redis.redisson;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RDeque;
import org.redisson.api.RFuture;
import org.redisson.api.RLexSortedSet;
import org.redisson.api.RList;
import org.redisson.api.RListMultimap;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RQueue;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RSetCache;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RSetMultimapCache;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;

public class RedissonDistributedCollectionsTest {
	
	public static void main(String[] args) {
		testMap();
		
		RedissonUtil.shutdown();
	}
	
	/**
	 * Map size limited by Redis to 4 294 967 295 elements.
	 */
	public static void testMap() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RMap<String, Object> map = redisson.getMap("anyMap");
		Object prevObject = map.put("123", new Object());
		Object currentObject = map.putIfAbsent("323", new Object());
		Object obj = map.remove("123");

		map.fastPut("321", new Object());
		map.fastRemove("321");

		RFuture<Object> putAsync = map.putAsync("321", new Object());
		RFuture<Boolean> fastPutAsync = map.fastPutAsync("321", new Object());
		RFuture<Long> fastRemoveAsync = map.fastRemoveAsync("321");
	}
	
	/**
	 * Current redis implementation doesn't has map entry eviction functionality. Therefore expired entries are cleaned by org.redisson.EvictionScheduler.
	 * It removes 100 expired entries at once. Task launch time tuned automatically and depends on expired entries amount deleted in previous time and varies between 1 second to 2 hours.
	 * Thus if clean task deletes 100 entries each time it will be executed every second (minimum execution delay).
	 * But if current expired entries amount is lower than previous one then execution delay will be increased by 1.5 times.
	 */
	public static void testMapEviction() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RMapCache<String, Object> map = redisson.getMapCache("anyMap");
		// ttl = 10 minutes, 
		map.put("key1", new Object(), 10, TimeUnit.MINUTES);
		// ttl = 10 minutes, maxIdleTime = 10 seconds
		map.put("key1", new Object(), 10, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);

		// ttl = 3 seconds
		map.putIfAbsent("key2", new Object(), 3, TimeUnit.SECONDS);
		// ttl = 40 seconds, maxIdleTime = 10 seconds
		map.putIfAbsent("key2", new Object(), 40, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);
	}
	
	/**
	 * Map local cache <br>
	 * In case then Map used mostly for read operations network roundtrips are undesirable. Redisson offers RLocalCachedMap object which caches Map entries on Redisson side.
	 * <p>
	 * need a higher version of redisson
	 */
	public static void testMapLocalCache() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		
//		LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
//			      // LFU, LRU and NONE policies are available
//			     .evictionPolicy(EvictionPolicy.LFU)
//			     .cacheSize(1000)
//			      // if `true` then invalidation message which removes corresponding entry from cache
//			      // will be sent to all other RLocalCachedMap instances on each entry update/remove operation
//			     .invalidateEntryOnChange(false)
//			      // time to live for each map entry in cache
//			     .timeToLive(10000)
//			      // or
//			     .timeToLive(10, TimeUnit.SECONDS)
//			      // max idle time for each map entry in cache
//			     .maxIdle(10000)
//			      // or
//			     .maxIdle(10, TimeUnit.SECONDS);
//		
//		RLocalCachedMap<String, Integer> map = redisson.getLocalCachedMap("test", options);
//
//		map.put("1", 1);
//		map.put("2", 2);
//		map.fastPut("3", 4);
//		
//		// Object should be destroyed if it not used anymore, but it's not necessary to call destroy method if Redisson goes shutdown.
//		map.destroy();
	}
	
	/**
	 * Map data partitioning <br>
	 * Map data partitioning for Java available only in cluster mode.
	 * <p>
	 * This feature available only in Redisson PRO edition.
	 */
	public static void testMapDataPartitioning() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
//		RClusteredMap<String, Object> map = redisson.getClusteredMap("anyMap");
//
//		Object prevObject = map.put("123", new Object());
//		Object currentObject = map.putIfAbsent("323", new Object());
//		Object obj = map.remove("123");
//
//		map.fastPut("321", new Object());
//		map.fastRemove("321");
	}
	
	/**
	 * Multimap <br>
	 * Keys amount limited by Redis to 4 294 967 295 elements.
	 * <p>
	 */
	public static void testMultimap() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		
		// Set based Multimap
		// Set based Multimap doesn't allow duplications for values per key.
		RSetMultimap<String, String> map = redisson.getSetMultimap("myMultimap");
		map.put("0", "1");
		map.put("0", "2");
		map.put("3", "4");
		Set<String> allValues = map.get("0");
		List<String> newValues = Arrays.asList("7", "6", "5");
		Set<String> oldValues = map.replaceValues("0", newValues);
		Set<String> removedValues = map.removeAll("0");
		
		// List based Multimap
		// List based Multimap for Java stores insertion order and allows duplicates for values mapped to key.
		RListMultimap<String, String> mapl = redisson.getListMultimap("test1");
		mapl.put("0", "1");
		mapl.put("0", "2");
		mapl.put("0", "1");
		mapl.put("3", "4");
		List<String> allValues1 = mapl.get("0");
		Collection<String> newValues1 = Arrays.asList("7", "6", "5");
		List<String> oldValues1 = mapl.replaceValues("0", newValues1);
		List<String> removedValues1 = mapl.removeAll("0");
		
		// Multimap eviction, 特征参考 testMapEviction()方法的注释
		// RListMultimapCache is similar with RSetMultimapCache
		RSetMultimapCache<String, String> mapc = redisson.getSetMultimapCache("myMultimap");
		mapc.put("1", "a");
		mapc.put("1", "b");
		mapc.put("1", "c");
		mapc.put("2", "e");
		mapc.put("2", "f");
		mapc.expireKey("2", 10, TimeUnit.MINUTES);
	}
	
	/**
	 * Set <br>
	 * Set size limited by Redis to 4 294 967 295 elements.
	 */
	public static void testSet() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RSet<String> set = redisson.getSet("anySet");
		set.add("1");
		set.remove("1");
		
		// Set eviction, 特征参考 testMapEviction()方法的注释
		RSetCache<String> setc = redisson.getSetCache("anySet");
		setc.add("2", 10, TimeUnit.SECONDS);
		
		// Set data partitioning
		// This feature available only in Redisson PRO edition.
//		RClusteredSet<String> setcl = redisson.getClusteredSet("anySet");
//		setcl.add("1");
//		setcl.remove("1");
	}
	
	/**
	 * SortedSet <br>
	 */
	public static void testSortedSet() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RSortedSet<Integer> set = redisson.getSortedSet("anySet");
//		set.trySetComparator(new MyComparator()); // set object comparator
		set.add(3);
		set.add(1);
		set.add(2);

		set.removeAsync(0);
		set.addAsync(5);
	}
	
	/**
	 * ScoredSortedSet <br>
	 * Sorts elements by score defined during element insertion.
	 */
	public static void testScoredSortedSet() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RScoredSortedSet<String> set = redisson.getScoredSortedSet("simple");
		set.add(0.13, "a");
		set.addAsync(0.251, "b");
		set.add(0.302, "c");
		
		set.pollFirst();
		set.pollLast();
		
		int index = set.rank("a"); // get element index
		Double score = set.getScore("a"); // get element score
	}
	
	/**
	 * LexSortedSet <br>
	 * Redisson distributed Set object for Java allows String objects only with lexicographical ordering.
	 */
	public static void testLexSortedSet() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RLexSortedSet set = redisson.getLexSortedSet("simple");
		set.add("d");
		set.addAsync("e");
		set.add("f");

		set.rangeTail("d", false);
		set.countHead("e", true);
		set.range("d", true, "z", false);
	}
	
	/**
	 * List <br>
	 * List size limited by Redis to 4 294 967 295 elements.
	 */
	public static void testList() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RList<String> list = redisson.getList("anyList");
		list.add("a");
		list.get(0);
		list.remove("a");
	}
	
	/**
	 * Queue <br>
	 * Queue size limited by Redis to 4 294 967 295 elements.
	 */
	public static void testQueue() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RQueue<String> queue = redisson.getQueue("anyQueue");
		queue.add("a");
		String obj = queue.peek();
		String someObj = queue.poll();
	}
	
	/**
	 * Deque <br>
	 * Deque size limited by Redis to 4 294 967 295 elements.
	 */
	public static void testDeque() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RDeque<Object> queue = redisson.getDeque("anyDeque");
		queue.addFirst(new Object());
		queue.addLast(new Object());
		Object obj = queue.removeFirst();
		Object someObj = queue.removeLast();;
	}
	
	/**
	 * Blocking Queue <br>
	 * BlockingQueue size limited by Redis to 4 294 967 295 elements.
	 * <p>
	 * poll, pollFromAny, pollLastAndOfferFirstTo and take methods will be resubscribed automatically during reconnection to Redis server or Redis server failover.
	 * 
	 * @throws InterruptedException 
	 */
	public static void testBlockingQueue() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBlockingQueue<Object> queue = redisson.getBlockingQueue("anyQueue");
		queue.offer(new Object());

		Object obj = queue.peek();
		Object someObj = queue.poll();
		Object ob = queue.poll(10, TimeUnit.MINUTES);
	}
	
	/**
	 * Bounded Blocking Queue <br>
	 * BoundedBlockingQueue size limited by Redis to 4 294 967 295 elements.
	 * <p>
	 * Queue capacity should be defined once before usage.
	 * <p>
	 * poll, pollFromAny, pollLastAndOfferFirstTo and take methods will be resubscribed automatically during reconnection to Redis server or Redis server failover.
	 * 
	 * @throws InterruptedException 
	 */
	public static void testBoundedBlockingQueue() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBoundedBlockingQueue<String> queue = redisson.getBoundedBlockingQueue("anyQueue");
		
		// returns `true` if capacity set successfully and `false` if it already set.
		queue.trySetCapacity(2);

		queue.offer("a");
		queue.offer("b");
		
		// waits for space to become available
		queue.put("c");

		String obj = queue.peek();
		String someObj = queue.poll();
		String ob = queue.poll(10, TimeUnit.MINUTES);
	}
	
	/**
	 * Blocking Deque <br>
	 * BlockingDeque size limited by Redis to 4 294 967 295 elements.
	 * <p>
	 * poll, pollFromAny, pollLastAndOfferFirstTo and take methods will be resubscribed automatically during reconnection to Redis server or Redis server failover.
	 * 
	 * @throws InterruptedException 
	 */
	public static void testBlockingDeque() throws InterruptedException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBlockingDeque<Integer> deque = redisson.getBlockingDeque("anyDeque");
		deque.putFirst(1);
		deque.putLast(2);
		Integer firstValue = deque.takeFirst();
		Integer lastValue = deque.takeLast();
		Integer firstValue1 = deque.pollFirst(10, TimeUnit.MINUTES);
		Integer lastValue1 = deque.pollLast(3, TimeUnit.MINUTES);
	}
	
}
