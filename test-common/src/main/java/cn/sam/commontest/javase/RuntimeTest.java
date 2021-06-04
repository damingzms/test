package cn.sam.commontest.javase;

import java.io.IOException;
import java.io.InputStream;

public class RuntimeTest {
	public static void main(String[] args) {
		RuntimeTest s = new RuntimeTest();
		s.test();
	}

	public void test() {
		Runtime run = Runtime.getRuntime();
		try {
			Process p = run.exec("java -verbose:class -version");
			InputStream ins = p.getInputStream();
//			InputStream ers = p.getErrorStream();
			new Thread(new inputStreamThread(ins)).start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class inputStreamThread implements Runnable {
		private InputStream ins = null;

		public inputStreamThread(InputStream ins) {
			this.ins = ins;
		}

		@Override
		public void run() {
			byte[] b = new byte[100];
			try {
				while (ins.read(b) != -1) {
					System.out.println(new String(b, "gb2312"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}