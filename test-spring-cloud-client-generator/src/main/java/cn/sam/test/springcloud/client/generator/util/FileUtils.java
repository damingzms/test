package cn.sam.test.springcloud.client.generator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.maven.plugin.logging.Log;

public class FileUtils {
	
	/**
	 * @param parent 可为空
	 * @param childs
	 * @return
	 * @throws  NullPointerException
     *          If <code>childs</code> is <code>null</code>
	 */
	public static File mkdirs(File parent, String... childs) {
		for (int i = 0; i < childs.length; i++) {
			parent = new File(parent, childs[i]);
			parent.mkdirs();
		}
		return parent;
	}
	
	/**
	 * @param log
	 * @param parent
	 * @param childs
	 * @return
	 * @throws  NullPointerException
     *          If <code>childs</code> is <code>null</code>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static FileOutputStream getFileOutputStream(Log log, File parent, String... childs) throws FileNotFoundException, IOException {
		File file = mkdirs(parent, childs);
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		return fos;
	}
	
	public static void flushAndClose(OutputStream os, Log log) throws IOException {
		if (os != null) {
			IOException exception = null;
			try {
				os.flush();
			} catch (IOException e) {
				log.error(e);
				exception = e;
			}
			try {
				os.close();
			} catch (IOException e) {
				log.error(e);
				if (exception == null) {
					exception = e;
				}
			}
			if (exception != null) {
				throw exception;
			}
		}
	}

}
