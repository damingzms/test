package cn.sam.test.springcloud.client.generator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;

public class TestJavapoet {

	public static void main(String[] args) {
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

}
