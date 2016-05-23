package cn.sam.commontest.nio.sse.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;

/**
 * @author tcowan
 */
public class SelectorFactory {
	public static Selector getSelector(int port)
		throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		Selector selector = Selector.open();
		serverChannel.socket().bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(false);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		return selector;
	}
}
