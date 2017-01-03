package cn.sam.commontest.javase;

public class TestTryCatch {
	
	public static void test1() {
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
	
	public static String test2() {
		try {
			throw new Exception();
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		} finally {
			return "finally";
		}
//		return "normal";
	}
	
	/**
	 * 
	 * @return finally
	 */
	public static String test3() {
		try {
			return "try";
		} catch (Exception e) {
			throw e;
		} finally {
			return "finally";
		}
	}
	
	/**
	 * 
	 * @return finally
	 */
	public static void test4() {
		for (int i = 0; i> 4; i++) {
			try {
				continue;
			} catch (Exception e) {
				throw e;
			} finally {
				System.out.println("finally");	// 不会输出
			}
		}
	}
	
	public static void main(String[] args) {
//		System.out.println(test3());
		test4();
	}

}
