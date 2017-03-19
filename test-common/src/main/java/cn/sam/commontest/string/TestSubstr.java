package cn.sam.commontest.string;

public class TestSubstr {
	
	public static void main(String[] args) {
		String str = "abcd";
		int length = str.length();
		System.out.println(length);
		String substring = str.substring(length);
		System.out.println(substring);
	}
	
}
