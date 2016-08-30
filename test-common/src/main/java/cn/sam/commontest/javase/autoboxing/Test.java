package cn.sam.commontest.javase.autoboxing;

public class Test {

	/**
	 * throw NullPointerException
	 * @return
	 */
	public static void test1() {
		Integer i = null;
		int i1 = 3;
		System.out.println(i1 == i);
	}

	/**
	 * @return true
	 */
	public static void test2() {
		Integer i = 128;
		int i1 = 128;
		System.out.println(i1 == i);
	}

	/**
	 * @return false
	 */
	public static void test3() {
		Integer i = 128;
		Integer i1 = 128;
		System.out.println(i1 == i);
	}

	/**
	 * @return true
	 */
	public static void test4() {
		Integer i = 127;
		Integer i1 = 127;
		System.out.println(i1 == i);
	}

	public static void main(String[] args) {
		test2();
	}
}
