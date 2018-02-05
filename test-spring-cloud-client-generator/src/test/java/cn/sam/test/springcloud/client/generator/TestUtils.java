package cn.sam.test.springcloud.client.generator;

import java.util.Map;

import cn.sam.test.springcloud.client.generator.bean.Bean;

public class TestUtils {

	public static void main(String[] args) throws Exception {
		Map<String, Object> bean2Map = Utils.bean2Map(new Bean());
		System.out.println(bean2Map);
	}

}
