package cn.sam.redis.pubsub;

import redis.clients.jedis.JedisPubSub;

public class Listener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		System.out.println("Method onMessage");
		System.out.println(channel + "=" + message);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		System.out.println("Method onSubscribe");
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		System.out.println("Method onUnsubscribe");
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		System.out.println("Method onPMessage");
		System.out.println(pattern + "=" + channel + "=" + message);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		System.out.println("Method onPSubscribe");
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		System.out.println("Method onPUnsubscribe");
	}

}
