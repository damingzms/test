package cn.sam.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * https://github.com/mrniko/redisson/wiki
 * <p>
 * Redisson configuration could be programmatic configuration or be loaded from JSON or YAML format.
 * 本示例只是简单的配置，详细配置请见以上官方文档。
 * 
 */
public final class RedissonUtil {
	
	private static final RedissonClient CLIENT;
	
	static {
		CLIENT = init_SingleInstanceMode_DefaultConfig();
	}
	
	public static RedissonClient getRedissonClient() {
		return CLIENT;
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
	

	private static RedissonClient MasterSlaveMode() {
		Config config = new Config();
		config.useMasterSlaveServers()
		    .setMasterAddress("127.0.0.1:6379")
		    .addSlaveAddress("127.0.0.1:6389", "127.0.0.1:6332", "127.0.0.1:6419")
		    .addSlaveAddress("127.0.0.1:6399");
		return Redisson.create(config);
	}
	
}
