package cn.sam.redis.redisson;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.redisson.api.Node;
import org.redisson.api.NodesGroup;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RScript;
import org.redisson.api.RScript.Mode;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisConnection;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.connection.ConnectionListener;

public class RedissonAdditionalFeaturesTest {
	
	public static void main(String[] args) throws Exception {
//		try {
////			testRedisNodes();
//			testScripting();
//			
//			// input any text to exit
//			System.out.println("input any text to exit:");
//			System.in.read();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			RedissonUtil.shutdown();
//		}
		
		testLowLevelRedisClient();
	}

	/**
	 * Redis nodes <br>
	 */
	public static void testRedisNodes() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		NodesGroup<Node> nodesGroup = redisson.getNodesGroup();
		nodesGroup.addConnectionListener(new ConnectionListener() {
		    public void onConnect(InetSocketAddress addr) {
		       // Redis server connected
		    	System.out.println("Connect: " + addr.toString());
		    }

		    public void onDisconnect(InetSocketAddress addr) {
		       // Redis server disconnected
		    	System.out.println("Disconnect: " + addr.toString());
		    }
		});
		
		Collection<Node> allNodes = nodesGroup.getNodes();
		for (Node n : allNodes) {
		    n.ping();
		    System.out.println(n.getAddr().toString());
		}
		// or
		boolean pingAll = nodesGroup.pingAll();
		System.out.println(pingAll);
	}

	/**
	 * Execution batches of commands <br>
	 * By using RBatch you can decrease time execution of command group. In Redis this approach called Pipelining.
	 */
	public static void testBatch() {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		RBatch batch = redisson.createBatch();
		batch.getMap("test").fastPutAsync("1", "2");
		batch.getMap("test").fastPutAsync("2", "3");
		batch.getMap("test").putAsync("2", "5");
		batch.getAtomicLong("counter").incrementAndGetAsync();
		batch.getAtomicLong("counter").incrementAndGetAsync();

		List<?> res = batch.execute();

		// or 

		Future<List<?>> asyncRes = batch.executeAsync();
	}

	/**
	 * Scripting <br>
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void testScripting() throws InterruptedException, ExecutionException, TimeoutException {
		RedissonClient redisson = RedissonUtil.getRedissonClient();
		redisson.getBucket("foo").set("bar");
		String r = redisson.getScript().eval(Mode.READ_ONLY, 
		   "return redis.call('get', 'foo')", RScript.ReturnType.VALUE);
		System.out.println(r);

		// do the same using cache
		RScript s = redisson.getScript();
		// load script into cache to all redis master instances
		String res = s.scriptLoad("return redis.call('get', 'foo')");
		// res == 282297a0228f48cd3fc6a55de6316f31422f5d17
		System.out.println(res);

		// call script by sha digest
		Future<Object> r1 = redisson.getScript().evalShaAsync(Mode.READ_ONLY, res, RScript.ReturnType.VALUE,
				Collections.emptyList());
		System.out.println(r1.get(5, TimeUnit.SECONDS));
	}

	/**
	 * Low level Redis client <br>
	 * You may use it if you want to execute a command not supported by Redisson yet. Anyway, try to find your command in Redis command mapping list before you use low-level client.
	 * 
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void testLowLevelRedisClient() throws InterruptedException, ExecutionException, TimeoutException {
		RedisClient client = new RedisClient("localhost", 6379);
		RedisConnection conn = client.connect();
		//or 
		Future<RedisConnection> connFuture = client.connectAsync();

		conn.sync(StringCodec.INSTANCE, RedisCommands.SET, "test", 0);
		RFuture<Object> async = conn.async(StringCodec.INSTANCE, RedisCommands.GET, "test");
		System.out.println(async.get(5, TimeUnit.SECONDS));

		String sync = conn.sync(RedisCommands.PING);
		System.out.println(sync);

		conn.closeAsync();
		
//		client.shutdown();
		// or
		client.shutdownAsync();
	}
	
}
