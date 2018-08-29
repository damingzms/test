package cn.sam.commontest.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Test1 {
	
	public static void test1() {
		List<Long> ids = new ArrayList<>();
		System.out.println(String.format("test: %s", StringUtils.join(ids.toArray(), ", ")));
	}
	
	public static void test2() {
		Map<String, Object> map = new HashMap<>();
		map.put(null, "1");
		map.put("key1", 2);
		System.out.println(map.get(null));
		System.out.println(map.get("key1"));
	}

	public static void main(String[] args) {
		test2();
	}

}
