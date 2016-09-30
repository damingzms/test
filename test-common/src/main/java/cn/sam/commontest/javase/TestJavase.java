package cn.sam.commontest.javase;

public class TestJavase {
	
	public static void test1() {
		System.out.println((Integer)128 == (Integer)128);
	}
	
	public static void test2() {
		Double d = null;
		System.out.println(d == null ? 2D : d + d);	// 2.0
		System.out.println((d == null ? 2D : d) + d);	// NullPointerException
	}
	
	public static void main(String[] args) {
		test2();
	}

}
