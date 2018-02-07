package cn.sam.commontest.javase.enums;

public class Test {

	public static void main(String[] args) {
//		Gender gender = Gender.valueOf("ff"); // java.lang.IllegalArgumentException: No enum constant cn.sam.commontest.javase.enums.Gender.ff
//		System.out.println(gender);
		Gender[] enumConstants = Gender.class.getEnumConstants();
		System.out.println(enumConstants);
	}

}
