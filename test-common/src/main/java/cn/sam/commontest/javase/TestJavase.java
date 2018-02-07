package cn.sam.commontest.javase;

import java.util.HashMap;
import java.util.Map;

public class TestJavase {
	
	public static void test1() {
		System.out.println((Integer)128 == (Integer)128);
	}
	
	public static void test2() {
		Double d = null;
		System.out.println(d == null ? 2D : d + d);	// 2.0
		System.out.println((d == null ? 2D : d) + d);	// NullPointerException
	}
	
	public static void test3() {
		Map<String, Integer> map = new HashMap<>();
		map.put("test", 1);
		Integer value = map.get("test");
		value++;
		System.out.println(map.get("test")); // 1
		
		value = value + 1;
		System.out.println(map.get("test")); // 1
	}
	
	public static void main(String[] args) {
		test3();
	}

}
