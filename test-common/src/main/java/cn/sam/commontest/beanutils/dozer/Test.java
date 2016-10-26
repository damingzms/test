package cn.sam.commontest.beanutils.dozer;

import java.util.ArrayList;

import org.dozer.DozerBeanMapper;

import cn.sam.commontest.beanutils.bean.Apple;
import cn.sam.commontest.beanutils.bean.Orange;

public class Test {
	public static final DozerBeanMapper MAPPER = new DozerBeanMapper();
	
	public static void test1() {
		Father f = new Father();
		f.setJob("engineer");
		
		Son son = MAPPER.map(f, Son.class);
		System.out.println(son.getJob());	// engineer
	}
	
	/**
	 * deep copy, 而且只是泛型
	 */
	public static void test2() {
		Apple a = new Apple();
		a.setColor("red");
		a.setOranges(new ArrayList<Orange>());
		
		Orange o = new Orange();
		o.setColor("blue");
		a.getOranges().add(o);
		System.out.println(o);
		
		Apple a2 = MAPPER.map(a, Apple.class);
		System.out.println(a2.getOranges().get(0));
	}

	public static void main(String[] args) {
		test2();
	}

}
