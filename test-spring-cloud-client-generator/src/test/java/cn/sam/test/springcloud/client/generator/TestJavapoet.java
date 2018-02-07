package cn.sam.test.springcloud.client.generator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import cn.sam.test.springcloud.client.generator.bean.TestInterface;

public class TestJavapoet {
	
	public static void test1() {
		MethodSpec main = MethodSpec.methodBuilder("main")
			    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
			    .returns(void.class)
			    .addParameter(String[].class, "args")
			    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
			    .build();
		MethodSpec test = MethodSpec.methodBuilder("test")
			    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
			    .returns(void.class)
			    .addParameter(String[].class, "args")
			    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
			    .build();
		System.out.println(main.toString() + test.toString());
	}
	
	public static void test2() {
		ClassName className = ClassName.get(TestInterface.class);
		System.out.println(className);
		ClassName className1 = ClassName.get("cn.sam.test.springcloud.client.generator.bean", "TestInterface");
		System.out.println(className1);
	}

	public static void main(String[] args) {
		test2();
	}

}
