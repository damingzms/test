package cn.sam.commontest.javase.inherence;

public class Test {

	public static void main(String[] args) {
		Son son = new Son();
		son.setAge(3);
		son.setJob("engineer");
		System.out.println(son.getJob());
		Father f = son;
		System.out.println(f.getJob());
	}

}
