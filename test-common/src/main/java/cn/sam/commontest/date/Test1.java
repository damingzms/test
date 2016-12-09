package cn.sam.commontest.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.time.DateUtils;

public class Test1 {
	
	public static void test1() {
		try {
			System.out.println(DateUtils.parseDate("2016-09-7 15:00:00", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void test2() {
		String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(null);	// NullPointerException
		System.out.println(format);
	}

	public static void main(String[] args) {
		test2();
	}

}
