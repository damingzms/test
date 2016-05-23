package cn.sam.commontest.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestAddAndRemove {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("a1");
		list.add("a2");
		list.add("a3");
		list.add("a4");
		list.add("a5");
		Iterator<String> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		list.add(3, "a6");
		System.out.println(list);
	}

}
