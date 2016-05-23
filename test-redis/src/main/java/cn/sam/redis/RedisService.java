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
public class RedisService {
	private static Properties properties;
	
	private static JedisPool jedisPool;
	
	private static ShardedJedisPool shardedJedisPool;
	
	private static ShardedJedisPool pShardedJedisPool;
	
	static {
		properties = new Properties();
		try {
			properties.load(RedisService.class.getResourceAsStream("/redis.properties"));

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
			
			// JedisPool
			jedisPool = new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT, password);
			
			// ShardedJedisPool
			JedisShardInfo info = new JedisShardInfo(host, port);
			info.setPassword(password);
			List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
			infoList.add(info);
			shardedJedisPool = new ShardedJedisPool(config, infoList);
			pShardedJedisPool = new ShardedJedisPool(config, infoList, ShardedJedis.DEFAULT_KEY_TAG_PATTERN);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeJedisPool() {
		jedisPool.close();
	}
	
	public static void closeShardedJedisPool() {
		shardedJedisPool.close();
	}
	
	public static void closePShardedJedisPool() {
		pShardedJedisPool.close();
	}
	
	public static Jedis getJedis() {
		return jedisPool.getResource();
	}
	
	public static ShardedJedis getShardedJedis() {
		return shardedJedisPool.getResource();
	}
	
	public static ShardedJedis getPShardedJedis() {
		return pShardedJedisPool.getResource();
	}
	
	public static void testJedis() {
		Jedis resource = getJedis();
		resource.hset("sam", "name", "zms");
		resource.hset("sam", "age", "22");
		System.out.println(resource.hget("sam", "age"));
		System.out.println(resource.hgetAll("sam"));
		resource.close();
		closeJedisPool();
	}
	
	public static void testShardedJedis() {
		ShardedJedis resource = getShardedJedis();
		resource.hset("ssam", "name", "zms");
		resource.hset("ssam", "age", "22");
		System.out.println(resource.hget("ssam", "age"));
		System.out.println(resource.hgetAll("ssam"));
		resource.close();
		closeShardedJedisPool();
	}
	
	public static void testPShardedJedis() {
		ShardedJedis resource = getPShardedJedis();
		resource.set("pfoo{bar}", "12345");
		System.out.println(resource.get("pfoo{bar}"));
		resource.close();
		closePShardedJedisPool();
	}
	
	public static void testTransaction() {
		Jedis jedis = getJedis();
		Transaction t = jedis.multi();
		t.set("fool", "bar");
		Response<String> result1 = t.get("fool");

		t.zadd("foo", 1, "barowitch");
		t.zadd("foo", 0, "barinsky");
		t.zadd("foo", 0, "barikoviev");
		Response<Set<String>> sose = t.zrange("foo", 0, -1);
		t.exec();

		String foolbar = result1.get();
		int soseSize = sose.get().size();
		System.out.println(foolbar);
		System.out.println(soseSize);
		jedis.close();
		closeJedisPool();
	}
	
	public static void testPipeline() {
		Jedis jedis = getJedis();
		Pipeline p = jedis.pipelined();
		p.set("fool", "bar");
		p.zadd("foo", 1, "barowitch");
		p.zadd("foo", 0, "barinsky");
		p.zadd("foo", 0, "barikoviev");
		Response<String> pipeString = p.get("fool");
		Response<Set<String>> sose = p.zrange("foo", 0, -1);
		p.sync();

		String foolbar = pipeString.get();
		Set<String> setBack = sose.get();
		System.out.println(foolbar);
		System.out.println(setBack.iterator().next());
		jedis.close();
		closeJedisPool();
	}
	
	public static void main(String[] args) {
		testPShardedJedis();
	}
}
