package cn.sam.commontest.javase.generictype;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class Test {
	
	public static void test0(double d) {
		System.out.println(d);
	}
	
	public static <T> void test1(T val) {
		test0((Integer)val);
		test0(Double.valueOf(String.valueOf(val)));
		test0(((Number)val).doubleValue());
		test0((double)val);	// exception
	}
	
	public static void test2() throws NoSuchMethodException, SecurityException {
		Method[] methods = Bean.class.getMethods();
		Class<?>[] parameterTypes = methods[0].getParameterTypes();
		TypeVariable<?>[] typeParameters = parameterTypes[0].getTypeParameters();
		System.out.println(typeParameters);
	}
	
	public static void test3() throws NoSuchMethodException, SecurityException {
		Field[] fields = Bean.class.getDeclaredFields();
		fields[0].setAccessible(true);
		Type genericType = fields[0].getGenericType();
		Type genericType1 = fields[1].getGenericType();
		Type genericType2 = fields[2].getGenericType();
		Type genericType3 = fields[3].getGenericType();
		Type genericType4 = fields[4].getGenericType();
		Type genericType5 = fields[5].getGenericType();
		Class<?> type = fields[0].getType();
		Class<?> type1 = fields[1].getType();
		Class<?> type2 = fields[2].getType();
		Class<?> type3 = fields[3].getType();
		Class<?> type4 = fields[4].getType();
		Class<?> type5 = fields[5].getType();
		TypeVariable<?>[] typeParameters = type.getTypeParameters();
		TypeVariable<?>[] typeParameters1 = type1.getTypeParameters();
		TypeVariable<?>[] typeParameters2 = type2.getTypeParameters();
		TypeVariable<?>[] typeParameters3 = type3.getTypeParameters();
		TypeVariable<?>[] typeParameters4 = type4.getTypeParameters();
		TypeVariable<?>[] typeParameters5 = type5.getTypeParameters();
		
		Type[] genericInterfaces = type.getGenericInterfaces();
		Type genericSuperclass = type.getGenericSuperclass();
		System.out.println(genericSuperclass);
	}

	public static void main(String[] args) throws Exception {
		test3();
	}

}
