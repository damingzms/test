package cn.sam.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 两种内存溢出异常[注意内存溢出是error级别的]
	1.StackOverFlowError:当请求的栈深度大于虚拟机所允许的最大深度
	2.OutOfMemoryError:虚拟机在扩展栈时无法申请到足够的内存空间[一般都能设置扩大]
 *
 * @author 12984
 *
 */
public class MemErrorTest {
	
	public static void main(String[] args) {
		try {
			List<Object> list = new ArrayList<Object>();
			for (;;) {
				list.add(new Object()); //用list保持着引用 防止full gc回收，OutOfMemoryError
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		try {
			hi();// 递归造成StackOverflowError 这边因为每运行一个方法将创建一个栈帧，栈帧创建太多无法继续申请到内存扩展
		} catch (StackOverflowError e) {
			e.printStackTrace();
		}
	}

	public static void hi() {
		hi();
	}
	
}