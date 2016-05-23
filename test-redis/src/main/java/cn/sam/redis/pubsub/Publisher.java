package cn.sam.redis.pubsub;

import redis.clients.jedis.Jedis;
import cn.sam.redis.RedisService;

public class Publisher {

	public static void main(String[] args) {
		Jedis jedis = RedisService.getJedis();
		jedis.publish("foo", "bar123");
		jedis.publish("hello_test", "hello watson");
		jedis.close();
	}

}
