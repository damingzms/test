package cn.sam.commontest.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.PatternMatchUtils;

public class TestPattern {
//	public static void main(String[] args) {
//		Pattern p=Pattern.compile("((\\d+)([a-z]+))"); 
//		Matcher m=p.matcher("aaa2223bb"); 
////		System.out.println("matches : " + m.matches());
////		System.out.println("lookingAt : " + m.lookingAt());
//		System.out.println("find : " + m.find());
////		System.out.println("find : " + m.find());
//		System.out.println(m.groupCount());
//		System.out.println("start : " + m.start(1));
//		System.out.println("end : " + m.end(1));
//		System.out.println("group : " + m.group(1));
//	}
	
//	public static void main(String[] args) {
//		Matcher matcher = Pattern.compile("\\w{2}\\s(\\w*)\\s*\\w{2}\\d").matcher("hu hik5566 HK1/pn");
//		if(matcher.find()){
//			System.out.println(matcher.group());
//			System.out.println(matcher.group(1));
//		}
//	}
	
//	public static void main(String[] args) {
//		boolean matches = "13755555555".matches("1[3578]\\d{9}");
//		System.out.println(matches);
//	}
	
//	public static void main(String[] args) {
//		boolean matches = "/test".matches("\\S*/test/\\S*");
//		System.out.println(matches);
//	}
	
	public static void main(String[] args) {
		String orderCodeStr = "abcd,efgh，ijk";
		String[] strings = orderCodeStr.split("[,，]");
		System.out.println(strings);
	}
	
}
