package cn.sam.commontest.javase;

public class TestPrecedence {

	public static void test1() {
		System.out.println(false && true || true);	// true
	}
	
	public static void main(String[] args) {
		test1();
	}
}
