package cn.sam.commontest.javase.generictype;

import java.util.List;
import java.util.Map;

public class Bean<T> {
	
	private Integer i;
	
	private Map<String, Integer> map;
	
	private T t;
	
	private List<? extends Bean> list;
	
	private String[] strArr;
	
	private T[] tArr;
	
	public void test(List<Integer> list) {
		
	}

}
