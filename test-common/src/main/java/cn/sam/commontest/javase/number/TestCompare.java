package cn.sam.commontest.javase.number;

public class TestCompare {

	public static void testDouble2Int() {
		double d = Integer.MAX_VALUE + 0.11;
		System.out.println(d > Integer.MAX_VALUE);	// true

		double d1 = Integer.MAX_VALUE;
		System.out.println(d1 == Integer.MAX_VALUE);	// true
	}
	
	public static void main(String[] args) {
		testDouble2Int();
	}
}
