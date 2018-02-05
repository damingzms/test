package cn.sam.test.conversion.object2map;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import cn.sam.test.conversion.object2map.bean.OtherPerson;
import cn.sam.test.conversion.object2map.bean.Son;

public class TestObject2Map {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Son son = new Son();
		son.setSonName("im son");
		OtherPerson op = new OtherPerson();
		op.setOtherName("im op");
		son.setOp(op);
		Map<String, String> describe = BeanUtils.describe(son);
		System.out.println(describe);
	}

}
