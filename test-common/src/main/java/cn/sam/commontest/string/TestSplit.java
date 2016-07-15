package cn.sam.commontest.string;

public class TestSplit {
	public static void main(String[] args) {
		String str = "asaa";
		String[] split = str.split(";");
		System.out.println(split.length);
	}
}
