package cn.sam.redis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

/**
 * Hello Redis!
 *
 */
public class RedisMasterSlaveService {
	
	private static JedisPool jedisPool1;
	
	private static JedisPool jedisPool2;
	
	static {
		try {
			init("/redis1.properties");
			init("/redis2.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void init(String cfgFile) throws IOException {
		Properties properties = new Properties();
		properties.load(RedisMasterSlaveService.class.getResourceAsStream(cfgFile));

		String host = properties.getProperty("redis.hostName");
		int port = 0;
		try {
			port = Integer.valueOf(properties.getProperty("redis.port"));
		} catch (NumberFormatException e) {
			port = Protocol.DEFAULT_PORT;
		}
		String password = properties.getProperty("redis.password");
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(Integer.valueOf(properties.getProperty("redis.maxIdle")));
		config.setTestOnBorrow("true".equalsIgnoreCase(properties.getProperty("redis.testOnBorrow")));
		
		JedisPool jedisPool = new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT, password);
		if (jedisPool1 == null) {
			jedisPool1 = jedisPool;
		} else {
			jedisPool2 = jedisPool;
		}
	}
	
	public static void closeJedisPool() {
		if (jedisPool1 != null) jedisPool1.close();
		if (jedisPool2 != null) jedisPool2.close();
	}
	
	public static Jedis getJedis1() {
		return jedisPool1.getResource();
	}
	
	public static Jedis getJedis2() {
		return jedisPool2.getResource();
	}
	
	public static void testJedis(Jedis jedis) {
		jedis.hset("sam", "name", "zms");
		jedis.hset("sam", "age", "22");
		System.out.println(jedis.hget("sam", "age"));
		System.out.println(jedis.hgetAll("sam"));
		jedis.close();
	}
	
	public static void testSlave(Jedis jedis1, Jedis jedis2) {
		String masterHost = jedis1.getClient().getHost();
		int masterPort = jedis1.getClient().getPort();
		System.out.println(jedis2.slaveofNoOne());
		jedis2.slaveof(masterHost, masterPort);
		jedis1.set("name", "sam");
		System.out.println(jedis2.get("name"));
		jedis1.close();
		jedis2.close();
	}
	
	public static void testSlave1(Jedis jedis1, Jedis jedis2) {
		String masterHost = jedis1.getClient().getHost();
		int masterPort = jedis1.getClient().getPort();
		jedis2.slaveof(masterHost, masterPort);
		
		for (int i = 1; i < 100; i++) {
			System.out.println("round:" + i);
			String key = "name" + i;
			try {
				jedis1.set(key, "sam" + i);
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(key + ":" + jedis2.get(key));
		}
		jedis1.close();
		jedis2.close();
	}
	
	
	public static void main(String[] args) {
		Jedis jedis2 = getJedis2();
		testJedis(jedis2);
		closeJedisPool();
	}
}
