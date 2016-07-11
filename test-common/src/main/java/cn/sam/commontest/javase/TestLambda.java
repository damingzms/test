package cn.sam.commontest.javase;

import java.util.Arrays;
import java.util.List;

public class TestLambda {
	
	public String sayHello() {
		System.out.println("Hello ! ");
		return "re hel";
	}

	public static void main(String[] args) {
		List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dave");
		names.forEach(e -> { System.out.println(e); });
	}

}
