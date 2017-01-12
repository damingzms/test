package cn.sam.commontest.javase.number;

public class TestTransformation {

	public static void testDouble2Int() {
		double d = 4.03;
		System.out.println((int) d);	// 4
		
		double d1 = Integer.MAX_VALUE + 0.11 + 13;
		System.out.println(d1);			//
		System.out.println((int) d1);	// Integer.MAX_VALUE
	}

	public static void testDouble2Long() {
		double d = 4.03;
		System.out.println((long) d);	// 4

		double d1 = Long.MAX_VALUE + 0.11 + 131313131313131313L;
		System.out.println(d1);			//
		System.out.println((long) d1);	// Long.MAX_VALUE
	}

	public static void testFloat2Long() {
		float d = 4.03F;
		System.out.println((long) d);	// 4

		float d1 = Long.MAX_VALUE + 0.11F + 131313131313131313L;
		System.out.println(d1);			//
		System.out.println((long) d1);	// Long.MAX_VALUE
	}
	
	public static void main(String[] args) {
		testFloat2Long();
	}
}
