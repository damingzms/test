package cn.sam.commontest.javase.generictype;

public class Test {
	
	public static void test0(double d) {
		System.out.println(d);
	}
	
	public static <T> void test1(T val) {
		test0((Integer)val);
		test0(Double.valueOf(String.valueOf(val)));
		test0(((Number)val).doubleValue());
		test0((double)val);	// exception
	}

	public static void main(String[] args) {
		Object o = Integer.valueOf(10);
		test1(o);
	}

}
