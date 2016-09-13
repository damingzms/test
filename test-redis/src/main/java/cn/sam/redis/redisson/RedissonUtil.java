package cn.sam.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

/**
 * https://github.com/mrniko/redisson/wiki
 * <p>
 * Configure -- 
 * https://github.com/mrniko/redisson/wiki/2.-Configuration
 * <p>
 * Redisson configuration could be programmatic configuration or be loaded from JSON or YAML format.
 * 本示例只是简单的配置，详细配置请见以上官方文档。
 * <p>
 * Redisson instance and Redisson objects are fully-thread safe.
 * <p>
 * Redis commands mapping: Redis command - Redisson method <br>
 * https://github.com/mrniko/redisson/wiki/11.-Redis-commands-mapping
 * <p>
 * Standalone node:
 * https://github.com/mrniko/redisson/wiki/12.-Standalone-node
 * <p>
 * Redis server is a single-threaded application.
 * 
 * <p>
 * <ul>
 * <li>1.RedissonOperationsTest.java
 * <li>2.RedissonDistributedObjectsTest.java 
 * <li>3.RedissonDistributedCollectionsTest.java
 * <li>4.RedissonDistributedLocksAndSynchronizersTest.java
 * <li>5.RedissonDistributedServicesTest.java
 * <li>6.RedissonAdditionalFeaturesTest.java
 * <li>7.RedissonIntegrationTest.java
 * </ul>
 */
public final class RedissonUtil {
	
	private static final RedissonClient CLIENT;
	
	private static final RedissonReactiveClient REACTIVE_CLIENT;
	
	static {
		CLIENT = init_SingleInstanceMode_CustomConfig();
		REACTIVE_CLIENT = init_RedissonReactiveClient();
	}
	
	public static RedissonClient getRedissonClient() {
		return CLIENT;
	}
	
	public static RedissonReactiveClient getRedissonReactiveClient() {
		return REACTIVE_CLIENT;
	}
	
	public static void shutdown() {
		CLIENT.shutdown();
		REACTIVE_CLIENT.shutdown();
	}
	
	/**
	 * Single instance mode
	 */
	
	private static RedissonClient init_SingleInstanceMode_DefaultConfig() {
		
		// connects to 127.0.0.1:6379 by default
		return Redisson.create();
	}
	
	private static RedissonClient init_SingleInstanceMode_CustomConfig() {
		Config config = new Config();
		config.useSingleServer().setAddress("localhost:6379");
		return Redisson.create(config);
	}
	
	/**
	 * Cluster mode
	 */

	private static RedissonClient init_ClusterMode() {
		Config config = new Config();
		config.useClusterServers()
		    .setScanInterval(2000) // cluster state scan interval in milliseconds
		    .addNodeAddress("127.0.0.1:7000", "127.0.0.1:7001")
		    .addNodeAddress("127.0.0.1:7002");
		return Redisson.create(config);
	}
	
	/**
	 * Master slave mode
	 */

	private static RedissonClient init_MasterSlaveMode() {
		Config config = new Config();
		config.useMasterSlaveServers()
		    .setMasterAddress("127.0.0.1:6379")
		    .addSlaveAddress("127.0.0.1:6389", "127.0.0.1:6332", "127.0.0.1:6419")
		    .addSlaveAddress("127.0.0.1:6399");
		return Redisson.create(config);
	}
	
	/**
	 * init RedissonReactiveClient
	 * @return
	 */
	private static RedissonReactiveClient init_RedissonReactiveClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("localhost:6379");
		return Redisson.createReactive(config);
	}
	
}
