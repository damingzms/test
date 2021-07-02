package cn.sam.commontest.classloader;

public class Test {
	
	public static void test1() {
		System.out.println(ClassLoader.getSystemClassLoader());
		System.out.println(ClassLoader.getSystemClassLoader().getParent());
		System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent());
	}
	
	public static void test2() throws ClassNotFoundException {
		System.out.println(System.getProperty("java.class.path"));
		Class<?> c = Class.forName("cn.sam.commontest.classloader.Test");
		System.out.println(c.getClassLoader());
	}

	public static void main(String[] args) throws ClassNotFoundException {
		test2();
	}

}
