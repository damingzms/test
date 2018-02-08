package cn.sam.commontest.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class TestReflect {
	
	public static void test1() {
		int[] temp = { 1, 2, 3, 4, 5 };
		Class<?> demo = temp.getClass().getComponentType();
		System.out.println("数组类型： " + demo.getName());
		System.out.println("数组类型： " + temp.getClass().getName());
	}
	
	public static void test2() throws NoSuchMethodException, SecurityException {
		Method method = MethodTestBean.class.getMethod("test");
		Type genericReturnType = method.getGenericReturnType();
		System.out.println(genericReturnType);   // class java.lang.Object
	}
	
	public static void test3() {
		System.out.println(Void.class == void.class);  // false
	}
	
	public static void test4() throws NoSuchMethodException, SecurityException {
		Method method = TestReflect.class.getMethod("test3");
		int modifiers = method.getModifiers();
		System.out.println(java.lang.reflect.Modifier.isPublic(modifiers));
		System.out.println(java.lang.reflect.Modifier.isPrivate(modifiers));
		System.out.println(java.lang.reflect.Modifier.isProtected(modifiers));
		System.out.println(java.lang.reflect.Modifier.isStatic(modifiers));
		System.out.println(java.lang.reflect.Modifier.isFinal(modifiers));
	}
	
	public static void test5() {
		System.out.println(TestReflect.class.getName());  // cn.sam.commontest.reflect.TestReflect
		System.out.println(TestReflect.class.getSimpleName());  // TestReflect
	}
	
	public static void main(String[] args) throws Exception {
		test5();
	}
}
