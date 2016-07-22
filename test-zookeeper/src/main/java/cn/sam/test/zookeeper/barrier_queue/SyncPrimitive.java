package cn.sam.test.zookeeper.barrier_queue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * http://zookeeper.apache.org/doc/trunk/zookeeperTutorial.html
 */
public class SyncPrimitive implements Watcher {

	static ZooKeeper zk = null;
	static Integer mutex;

	String root;

	SyncPrimitive(String address) {
		if (zk == null) {
			try {
				System.out.println("Starting ZK:");
				zk = new ZooKeeper(address, 3000, this);
				mutex = new Integer(-1);
				System.out.println("Finished starting ZK: " + zk);
			} catch (IOException e) {
				System.out.println(e.toString());
				zk = null;
			}
		}
		// else mutex = new Integer(-1);
	}

	synchronized public void process(WatchedEvent event) {
		synchronized (mutex) {
			// System.out.println("Process: " + event.getType());
			mutex.notify();
		}
	}

	/**
	 * Barrier
	 */
	static public class Barrier extends SyncPrimitive {
		int size;
		String name;

		/**
		 * Barrier constructor
		 *
		 * @param address
		 * @param root
		 * @param size
		 */
		Barrier(String address, String root, int size) {
			super(address);
			this.root = root;
			this.size = size;

			// Create barrier node
			if (zk != null) {
				try {
					Stat s = zk.exists(root, false);
					if (s == null) {
						zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				} catch (KeeperException e) {
					System.out.println("Keeper exception when instantiating queue: " + e.toString());
				} catch (InterruptedException e) {
					System.out.println("Interrupted exception");
				}
			}

			// My node name
			try {
				name = new String(InetAddress.getLocalHost().getCanonicalHostName().toString());
			} catch (UnknownHostException e) {
				System.out.println(e.toString());
			}

		}

		/**
		 * Join barrier
		 *
		 * @return
		 * @throws KeeperException
		 * @throws InterruptedException
		 */

		boolean enter() throws KeeperException, InterruptedException {
			zk.create(root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			while (true) {
				synchronized (mutex) {
					List<String> list = zk.getChildren(root, true);

					if (list.size() < size) {
						mutex.wait();
					} else {
						return true;
					}
				}
			}
		}

		/**
		 * Wait until all reach barrier
		 *
		 * @return
		 * @throws KeeperException
		 * @throws InterruptedException
		 */

		boolean leave() throws KeeperException, InterruptedException {
			zk.delete(root + "/" + name, 0);
			while (true) {
				synchronized (mutex) {
					List<String> list = zk.getChildren(root, true);
					if (list.size() > 0) {
						mutex.wait();
					} else {
						return true;
					}
				}
			}
		}
	}

	/**
	 * Producer-Consumer queue
	 * 
	 * 假设有两个消费者在等待，然后一个生产者新增一个znode，此时两个消费者都会受到通知，并且很有可能都把这个znode查询出来，
	 * 因此其中会有一个消费者delete znode时会抛出KeeperException，
	 * 如此一来，如果有多个消费者同事在等等，可能会有一点宽带上浪费，而且实际使用中需要做KeeperException处理。
	 */
	static public class Queue extends SyncPrimitive {

		/**
		 * Constructor of producer-consumer queue
		 *
		 * @param address
		 * @param name
		 */
		Queue(String address, String name) {
			super(address);
			this.root = name;
			// Create ZK node name
			if (zk != null) {
				try {
					Stat s = zk.exists(root, false);
					if (s == null) {
						zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				} catch (KeeperException e) {
					System.out.println("Keeper exception when instantiating queue: " + e.toString());
				} catch (InterruptedException e) {
					System.out.println("Interrupted exception");
				}
			}
		}

		/**
		 * Add element to the queue.
		 *
		 * @param i
		 * @return
		 */

		boolean produce(int i) throws KeeperException, InterruptedException {
			ByteBuffer b = ByteBuffer.allocate(4);
			byte[] value;

			// Add child with value i
			b.putInt(i);
			value = b.array();
			zk.create(root + "/element", value, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

			return true;
		}

		/**
		 * Remove first element from the queue.
		 *
		 * @return
		 * @throws KeeperException
		 * @throws InterruptedException
		 */
		int consume() throws KeeperException, InterruptedException {
			int retvalue = -1;
			Stat stat = null;

			// Get the first element available
			while (true) {
				synchronized (mutex) {
					List<String> list = zk.getChildren(root, true);
					if (list.size() == 0) {
						System.out.println("Going to wait");
						mutex.wait();
					} else {
						String minStr = list.get(0).substring(7);
						Integer min = new Integer(minStr);
						for (String s : list) {
							String str = s.substring(7);
							Integer tempValue = new Integer(str);
							// System.out.println("Temporary value: " +
							// tempValue);
							if (tempValue < min) {
								min = tempValue;
								minStr = str;
							}
						}
						System.out.println("Temporary value: " + root + "/element" + minStr);
						byte[] b = zk.getData(root + "/element" + minStr, false, stat);
						zk.delete(root + "/element" + minStr, 0);
						ByteBuffer buffer = ByteBuffer.wrap(b);
						retvalue = buffer.getInt();

						return retvalue;
					}
				}
			}
		}
	}

	public static void main(String args[]) {
		args = new String[4];
		args[0] = "qTest";
		args[1] = "localhost:2181,localhost:2182,localhost:2183";
		args[2] = "2";
		args[3] = "c";
		
		if (args[0].equals("qTest"))
			queueTest(args);
		else
			barrierTest(args);

	}

	public static void queueTest(String args[]) {
		Queue q = new Queue(args[1], "/app1");

		System.out.println("Input: " + args[1]);
		int i = 0;
		Integer max = new Integer(args[2]);

		if (args[3].equals("p")) {
			System.out.println("Producer");
//			for (i = 0; i < max; i++)
				try {
					q.produce(10 + i);
				} catch (KeeperException e) {

				} catch (InterruptedException e) {

				}
		} else {
			System.out.println("Consumer");

//			for (i = 0; i < max; i++)
				try {
					int r = q.consume();
					System.out.println("Item: " + r);
				} catch (KeeperException e) {
					i--;
				} catch (InterruptedException e) {

				}
		}
	}

	public static void barrierTest(String args[]) {
		Barrier b = new Barrier(args[1], "/b1", new Integer(args[2]));
		try {
			boolean flag = b.enter();
			System.out.println("Entered barrier: " + args[2]);
			if (!flag)
				System.out.println("Error when entering the barrier");
		} catch (KeeperException e) {

		} catch (InterruptedException e) {

		}

		// Generate random integer
		Random rand = new Random();
		int r = rand.nextInt(100);
		// Loop for rand iterations
		for (int i = 0; i < r; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
		try {
			b.leave();
		} catch (KeeperException e) {

		} catch (InterruptedException e) {

		}
		System.out.println("Left barrier");
	}
}