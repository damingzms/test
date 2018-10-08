package cn.sam.commontest.javase.number;

import java.math.BigDecimal;

public class Test {
	
	private static void test1() {
		BigDecimal bd = new BigDecimal(1.01);
		System.out.println(bd.scale());
		System.out.println(bd.precision());
		
		BigDecimal bd1 = new BigDecimal(10000);
		System.out.println(bd1.scale());
		System.out.println(bd1.precision());
	}
	
	private static void test2() {
		double d = 6.56500;
		BigDecimal bd = new BigDecimal(d);
		System.out.println(bd.scale());
		System.out.println(bd.precision());
		
		String s = String.valueOf(d);
		System.out.println(s);
	}
	
	private static void test3() {
		Long i = 100L;
		Double d = ((Number) i).doubleValue();
		System.out.println(d);
		
		String s = String.valueOf(d);
		String[] split = s.split("[.]");
		System.out.println(split[0]);
		System.out.println(split[1]);
	}
	
	private static void test4() {
		Byte b = new Byte((byte) 4);
		System.out.println(b.equals(4));
	}
	
	public static boolean isNullOrZero(Number i) {
		return i == null || i.doubleValue() == 0;
	}

	public static void main(String[] args) {
		test4();
	}

}
