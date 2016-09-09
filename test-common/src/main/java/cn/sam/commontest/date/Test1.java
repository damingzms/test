package cn.sam.commontest.date;

import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;

public class Test1 {

	public static void main(String[] args) {
		try {
			System.out.println(DateUtils.parseDate("2016-09-7 15:00:00", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
