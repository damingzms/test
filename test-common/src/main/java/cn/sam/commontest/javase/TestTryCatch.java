package cn.sam.commontest.javase;

public class TestTryCatch {
	
	public static void main(String[] args) {
		try {
			System.out.println("try");
			double d = 4 / 0;
		} catch (Exception e) {
			System.out.println("catch");
			e.printStackTrace();
			double d = 4 / 0;
		} finally {
			System.out.println("finally");
		}
	}

}
