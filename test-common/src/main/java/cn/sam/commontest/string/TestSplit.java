package cn.sam.commontest.string;

public class TestSplit {
	
//	public static void main(String[] args) {
//		String str = "asaa";
//		String[] split = str.split(";");
//		System.out.println(split.length);
//	}
	
	public static void main(String[] args) {
		String str = "123:1234|111:222";
		String[] split = str.split("[|]");
		System.out.println(split.length);
		System.out.println(split[0].split(":").length);
	}
	
}
