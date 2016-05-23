package cn.sam.commontest.nio.sse.server;

import java.io.*;
import java.io.IOException;

/**
 * @author Taylor Cowan
 * @see /1test/src/cn/sam/test/jdk/nio/sse/run.bat
 */
public class Start {
	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(args[0]);
		final Server s = new Server(port, new ServerEventHandler(args[1],4));
		System.out.println("Starting server on port " + port);
		System.out.println("Web root folder: " + new File(args[1]).getAbsolutePath());
		while (true)
			s.listen();
	}
}
