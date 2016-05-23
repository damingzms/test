package cn.sam.commontest.reflect;

import java.lang.reflect.Modifier;

public class TestReflect {
	public static void main(String[] args) {
		int[] temp = { 1, 2, 3, 4, 5 };
		Class<?> demo = temp.getClass().getComponentType();
		System.out.println("数组类型： " + demo.getName());
		System.out.println("数组类型： " + temp.getClass().getName());
	}
}
