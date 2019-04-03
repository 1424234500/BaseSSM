package com.walker.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class TimeUtil {
	
	
	/**
	 * 解获取时间yyyyMMddHHmmss
	 */
	public static String getTimeSequence() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String res = sdf.format(new Date());
		return res;
	}

	/**
	 * 获取当前时间 HH:mm:ss
	 */
	public static String getNowTimeHms() {
		return getTime("HH:mm:ss");
	}

	/**
	 * 获取当前时间 yyyy-MM-dd
	 */
	public static String getTimeYmd() {
		return getTime("yyyy-MM-dd");
	}

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String getTimeYmdHms() {
		return getTime("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss:sss
	 */
	public static String getTimeYmdHmss() {
		return getTime("yyyy-MM-dd HH:mm:ss:SSS");
	}

	/**
	 * 获取指定格式的时间yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static String getTime(String format) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	/**
	 * 获取指定格式的时间yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static String getTime(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	/**
	 * 获取指定格式的时间yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static String getTime(long time, String format) {
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	
	/**
	 * 获取差值几天的 指定格式的时间yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static String getTime(String format, int detaDays) {
		long t = System.currentTimeMillis() + detaDays * 24 * 3600 * 1000;
		Date d = new Date(t);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	
	/**
	 * 格式化时间 yyyy-MM-dd
	 */
	public static String format(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String res = sdf.format(d);
		return res;
	}

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String formatL(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res = sdf.format(d);
		return res;
	}
	/**
	 * 2018-91-12, yyyy-MM-dd
	 * @param time
	 * @param format
	 * @return
	 */
	public static Date format(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date res = new Date();
		try {
			res = sdf.parse(time);
		} catch (ParseException e) {
			 e.printStackTrace();
		}
		return res;
	}


	/**
	 * 取得当月天数
	 * */
	public static int getDays() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 得到指定月的天数201901
	 * */
	public static int getDays(String yyyymmdd) {
		int year = Integer.parseInt(yyyymmdd.substring(0, 4));
		int month = Integer.parseInt(yyyymmdd.substring(5, 7));

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 获取时间戳
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
}
