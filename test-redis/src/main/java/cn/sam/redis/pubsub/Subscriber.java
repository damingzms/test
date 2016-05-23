package cn.sam.redis.pubsub;

import cn.sam.redis.RedisService;
import redis.clients.jedis.Jedis;

public class Subscriber {

	public static void main(String[] args) {
		final Jedis jedis = RedisService.getJedis();
		final Listener listener = new Listener();
		// 可以订阅多个频道
		// 订阅得到信息在lister的onMessage(...)方法中进行处理
		// 这里启动了订阅监听，线程将在这里被阻塞
//		jedis.subscribe(listener, "foo", "watson");

		// 订阅得到信息在lister的onPMessage(...)方法中进行处理
		// 这里启动了订阅监听，线程将在这里被阻塞
		jedis.psubscribe(listener, "hello_*");
		jedis.close();
	}

}
