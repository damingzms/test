package cn.sam.commontest.javase.enums;

import java.lang.reflect.Field;

public class Test {

	public static void main(String[] args) {
		Field[] fields = Gender.class.getDeclaredFields();
		System.out.println(fields.length);
	}

}
