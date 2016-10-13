package cn.sam.commontest.beanutils.apache;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import cn.sam.commontest.beanutils.bean.Apple;
import cn.sam.commontest.beanutils.bean.Orange;

public class TestBeanUtilsApache {
	
	public static void test1() throws Exception {
		Orange o = new Orange();
		o.setColor("red");
		o.setWeight(10);
		o.setAttr("qq");
		
//		Apple a1 = new Apple();
//		a1.setColor("green");
//		a1.setWeight(3);
//		
//		Apple a2 = new Apple();
//		a2.setColor("brue");
//		a2.setWeight(4);
//		
//		List<Apple> list = new ArrayList<>();
//		list.add(a1);
//		list.add(a2);
//		o.setApple(a1);
//		o.setApples(list);
		
//		Orange o1 = new Orange();
//		BeanUtils.copyProperties(o1, o);
//		PropertyUtils.copyProperties(o1, o);
		
		Apple a = new Apple();
		a.setPrice(100.0);
		BeanUtils.copyProperties(a, o);
//		PropertyUtils.copyProperties(a, o);
		
//		org.springframework.beans.BeanUtils.copyProperties(o, o1);
		org.springframework.beans.BeanUtils.copyProperties(o, a);
		
//		PropertyUtils.copyProperties(a1, a);
//		PropertyUtils.copyProperties(o, a);
//		BeanUtils.copyProperties(a2, o);
//		BeanUtils.copyProperties(o, a);
	}
	
	public static void test2() throws Exception {
		Orange o = new Orange();
		o.setColor("red");
		o.setWeight(10);
		o.setAttr("qq");
		o.setApple(new Apple());
		
		Orange o1 = new Orange();
//		BeanUtils.copyProperties(o1, o);
		PropertyUtils.copyProperties(o1, o);

		System.out.println(o1.getApple());
		System.out.println(o1.getApple() == o.getApple());	// true

		Orange o2 = new Orange();
		org.springframework.beans.BeanUtils.copyProperties(o, o2);
		System.out.println(o2.getApple());
		System.out.println(o2.getApple() == o.getApple());	// true
	}

	public static void main(String[] args) throws Exception {
		test2();
	}

}
