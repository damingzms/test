package cn.sam.commontest.collection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Test1 {

	public static void main(String[] args) {
		List<Long> ids = new ArrayList<>();
		System.out.println(String.format("test: %s", StringUtils.join(ids.toArray(), ", ")));
	}

}
