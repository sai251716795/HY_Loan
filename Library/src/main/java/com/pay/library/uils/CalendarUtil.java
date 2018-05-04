package com.pay.library.uils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前日期
		return format.format(curDate);
	}

	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return format.format(curDate);
	}

	public static Boolean lessThan(String dateF, String dateS) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (format.parse(dateF).getTime() < format.parse(dateS).getTime()) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getLastMonthDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date curDate = getLastDate(date);
		return format.format(curDate);
	}

	private static Date getLastDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	public static long reduce(String startDate, String endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		long result = 0;
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(startDate);
			d2 = format.parse(endDate);
			// 毫秒ms
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000;
			result = diffSeconds;
			/*
			 * long diffMinutes = diff / (60 * 1000) % 60; long diffHours = diff
			 * / (60 * 60 * 1000) % 24; long diffDays = diff / (24 * 60 * 60 *
			 * 1000); result =
			 * diffDays+"天"+diffHours+"小时"+diffMinutes+"分钟"+diffSeconds+"秒";
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String parseToStr(long diff) {
		long diffSeconds = diff % 60;
		long diffMinutes = diff / (60) % 60;
		long diffHours = diff / (60 * 60) % 24;
		long diffDays = diff / (24 * 60 * 60);
		return diffDays + "天" + diffHours + "小时" + diffMinutes + "分钟"
				+ diffSeconds + "秒";
	}
	
	
	
	
	public static String getYYMMDDHHMMSS(Date date){
		
		String time="";
		DateFormat format=new SimpleDateFormat("yyMMddHHmmss");
		time=format.format(date);
		return time;
	}

}
