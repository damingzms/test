package cn.sam.commontest.reflect;

import java.lang.reflect.Method;

import org.springframework.transaction.annotation.Transactional;

public class TestReflectAnnotation {
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Transactional annotation1 = Bean1.class.getAnnotation(Transactional.class);
		System.out.println(annotation1.readOnly());
		Transactional annotation2 = Bean2.class.getAnnotation(Transactional.class);
		System.out.println(annotation2.readOnly());
		
		System.out.println(annotation1 == annotation2);
		
//		Transactional annotation3 = Bean2.class.getDeclaredAnnotation(Transactional.class);
//		System.out.println(annotation3.readOnly()); // NullPointerException
		
		Method method = Bean1.class.getDeclaredMethod("test1", new Class<?>[0]);
		System.out.println(method);
		Transactional annotationM = method.getAnnotation(Transactional.class);
		System.out.println(annotationM);
	}
	
}
