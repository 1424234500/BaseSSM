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

public class Tools {
	
	
	public static void regex(String[] str, String regex){
		//编译正则
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		//使用正则匹配
		java.util.regex.Matcher matcher = pattern.matcher("");
		
		//matcher.reset(); //重置匹配位置
		for(String item : str){
			matcher.reset(item); //新匹配str
			while(matcher.find()){
				out(matcher.group());
			}
			
		}
		
		
	}

	public static String toString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
	
	public static String fillInt(Object obj, int len){
		return fillStringBy(obj+"", " ", len, 1);
	}
	public static String fillInt(Object obj){
		return fillInt(obj, 5);
	}


	private static int toolong = 600;

	public static String tooLongCut(String str) {
		if (str.length() > toolong)
			return "len." + str.length() + " size."
					+ Tools.calcSize(str.length()) + str.substring(0, toolong);
		return str;
	}

	public static String cutString(String str, int len) {
		if (str != null && str.length() > len) {
			str = str.substring(0, len);
		}
		return str;
	}

	/**
	 * 通过字符串长度，计算大概的 流量大小 MB KB B char=B
	 */
	public static String calcSize(int filesize) {
		return calcSize((long) filesize);
	}

	/**
	 * 通过字符串长度，计算大概的 流量大小 MB KB B char=B
	 */
	public static String calcSize(long filesize) {
		return filesize > 1024 * 1024 * 1024 ? (float) (10 * filesize / (1024 * 1024 * 1024))
				/ 10 + "G"
				: (filesize > 1024 * 1024 ? filesize / 1024 / 1024 + "M"
				: filesize / 1024 + "K");
	}

	/**
	 * ms计算耗时 10M8S100ms
	 */
	public static String calcTime(long filesize) {
		return filesize > 60 * 1000 ? (float) (10 * filesize / (60 * 1000))
				/ 10 + "M " + filesize % (60 * 1000) / 1000 + "S "
				: (filesize > 1000 ? filesize / 1000 + "S " + filesize % 1000
				+ "Ms " : filesize + "Ms ");
	}

	public static String getValueEncoded(String value) {
		if (value == null)
			return "null";
		String newValue = value.replace("\n", "");
		try {
			return URLEncoder.encode(newValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static Logger log = Logger.getLogger("Tools"); 
	public static void out(String str) {
//		System.out.println(getNowTimeS() + "." + Thread.currentThread().getName()+ "-" + Thread.currentThread().getId() + "." + str);
		log.info(str);;
	}

	public static void out(Object... objects) {
//		out(Arrays.toString(objects));
		out(objects2string(objects));
	}
	public static <T> void formatOut(Collection<T> list){
		int i = 0;
		for(T obj : list){
			out(i++, obj);
		}
	}

	public static String strings2string(String[] strs) {
		StringBuilder res = new StringBuilder("[ ");
		for (String str : strs) {
			res.append(str + ", ");
		}
		if (strs != null && strs.length > 0) {
			res.substring(0, res.length() - 2);
		}
		res.append(" ]");

		return res.toString();
	}

	public static String objects2string(Object... objects) {
		String[] res = objects2strings(objects);
//		return strings2string(res);
		return Arrays.toString(res);
	}

	//传入数组 作为动态参数 则也会 变为动参 除非手动上转为Object
	public static String[] objects2strings(Object... objects) {
//		return Arrays.asList(objects).toArray(new String[0]); //无法解决 序列动态参数为 数组的情况
		if (objects == null)
			return new String[]{""};
		String[] objs = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				if(objects[i] instanceof Object[]){
					objs[i] = objects2string((Object[])objects[i]);
				}else{
					objs[i] = objects[i].toString();
				}
			} else
				objs[i] = "null!";
		}
		return objs;
	}

	/**
	 * 解析 yyyy-MM-dd
	 */
	public static java.util.Date getDate(String s) {
		try {
			java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			return new java.util.Date(d.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析 yyyy-MM-dd HH:mm:ss
	 */
	public static java.sql.Timestamp getDateL(String s) {
		try {
			java.util.Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(s);
			return new java.sql.Timestamp(d.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 都不为null && ” “
	 */
	public static boolean notNull(Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null || objects[i].toString().equals("")) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 都为null || ” “
	 */
	public static boolean isNull(Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null && !objects[i].toString().equals("")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 空行 x i
	 */
	public static String getLines(int i) {
		String res = "";
		for (int j = 0; j < i; j++) {
			res += "-\n";
		}
		return res;
	}

	/**
	 * 空格 x i
	 */
	public static String getSpace(int i) {
		String res = "";
		for (int j = 0; j < i; j++) {
			res += " ";
		}
		return res;
	}

	/**
	 * str x i
	 */
	public static String getFill(int i, String str) {
		String res = "";
		for (int j = 0; j < i; j++) {
			res += str;
		}
		return res;
	}

	/**
	 * (12, " ", 6, 0)
	 * priorOrNext 1标识 在后面填充  0表示前面留空
	 *
	 */
	public static String fillStringBy(String str, String by, int tolen, int priorOrNext) {
		if (str.length() > tolen) {
			str = str.substring(0, tolen);
		} else {
			StringBuilder sb = new StringBuilder();
			int fromlen = str.length();
			for (int i = 0; i < (tolen - fromlen) / by.length(); i++) {
				sb.append(by);
			}
			if (priorOrNext == 0) {
				sb.reverse();
				str = sb.toString() + str;
			}else{
				str = str + sb.toString();
			}
		}
		return str;
	}

	/**
	 * 获取随机数，从到，补齐位数
	 */
	public static String getRandomNum(int fromNum, int toNum, int num) {
		int ii = (int) (Math.random() * (toNum - fromNum) + fromNum);
		String res = "" + ii;
		for (int i = res.length(); i < num; i++) {
			res = "0" + res;
		}
		return res;
	}

	/**
	 * 获取数组 索引
	 */
	public static <TYPE> int getCount(TYPE[] array, TYPE str) {
		if (array == null)
			return -1;
		if (str == null)
			return -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(str)) {
				return i;
			}
		}
		return -1;
	}

	public static Long parseLong(String num) {
		return parseLong(num, 0L);
	}

	/**
	 * 解析数字
	 */
	public static Long parseLong(String num, Long defaultValue) {
		if(Tools.isNull(num))return defaultValue;
		long res = 0;
		num = filterNum(num);
		if (!Tools.notNull(num)) {
			res = defaultValue;
		} else {
			try {
				res = Long.parseLong(num);
			} catch (Exception e) {
				Tools.out("解析:" + num + "数字失败");
				res = defaultValue;
			}
		}
		return res;
	}

	/**
	 * 解析数字
	 */
	public static Double parseDouble(String num) {
		return parseDouble(num, 0D);
	}
	public static Double parseDouble(String num, Double defaultValue) {
		double res = 0;
		num = filterNum(num);
		if (!Tools.notNull(num)) {
			res = defaultValue;
		} else {
			try {
				res = Double.parseDouble(num);
			} catch (Exception e) {
				Tools.out("解析:" + num + "数字失败");
				res = defaultValue;
			}
		}
		return res;
	}
	public static Float parseFloat(String num) {
		return parseFloat(num, 0F);
	}
	public static Float parseFloat(String num, Float defaultValue) {
		Float res = defaultValue;
		num = filterNum(num);
		if (!Tools.notNull(num)) {
			res = defaultValue;
		} else {
			try {
				res = Float.parseFloat(num);
			} catch (Exception e) {
				Tools.out("解析:" + num + "数字失败");
				res = defaultValue;
			}
		}
		return res;
	}
	/**
	 * 解析数字
	 */
	public static int parseInt(String num) {
		return parseInt(num, 0);
	}
	public static String filterNum(String num){
		//字符串筛选 筛选出特殊字符 [ ' ' ] - 这类 0-21-23 [133]  [^(\\d)]+
		return num.replaceAll("-+|\\s+|\\[+|\\]+|L$|D$|F$|l$|d$|f$", "");
	}
	/**
	 * 解析数字
	 */
	public static int parseInt(String num, Integer defaultValue) {
		if(Tools.isNull(num)) return defaultValue;
		int res = 0;
		num = filterNum(num);
		if (Tools.isNull(num)) {
			res = defaultValue;
		} else {
			try {
				res = Integer.parseInt(num);
			} catch (Exception e) {
				Tools.out("解析:" + num + "数字失败");
				res = defaultValue;
			}
		}
		return res;
	}
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
	public static String getNowTimeS() {
		return getTime("HH:mm:ss");
	}

	/**
	 * 获取当前时间 yyyy-MM-dd
	 */
	public static String getNowTime() {
		return getTime("yyyy-MM-dd");
	}

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowTimeL() {
		return getTime("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss:sss
	 */
	public static String getNowTimeLS() {
		return getTime("yyyy-MM-dd HH:mm:ss:SSS");
	}
	/**
	 * 格式化时间 yyyy-MM-dd
	 */
	public static String format(java.util.Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String res = sdf.format(d);
		return res;
	}

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String formatL(java.util.Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res = sdf.format(d);
		return res;
	}

	public static Date format(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date res = new Date();
		try {
			res = sdf.parse(time);
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取指定格式的时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getTime(String format) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	/**
	 * 取得当月天数
	 * */
	public static int getDaysNow() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 得到指定月的天数
	 * */
	public static int getDays(String yyyymmdd) {
		int year = parseInt(yyyymmdd.substring(0, 4));
		int month = parseInt(yyyymmdd.substring(5, 7));

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

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
	 */
	public static byte[] int2bytes(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (value >> (24 - i * 8));
		}
		return b;
	}

	public static int bytes2int(byte[] b) {
		return (((int) b[0] << 24) + (((int) b[1]) << 16) + (((int) b[2]) << 8) + b[3]);
	}

	/**
	 * 颜色16进制转换
	 */
	public static String color2string(int r, int g, int b) {
		String res = Tools.fillStringBy(Integer.toHexString(r) + "", "0", 2, 0)
				+ Tools.fillStringBy(Integer.toHexString(g) + "", "0", 2, 0)
				+ Tools.fillStringBy(Integer.toHexString(b) + "", "0", 2, 0);
		return res;
	}

	/**
	 * 获取当前ip所在网段的主机
	 */
	public static String getServerIp(String localIp) {
		String ip = localIp;
		if (ip != null) {
			if (ip.length() >= 7) {// 231.132.131.n?,wifi主机往往那个是尾数为1
				String ss[] = ip.split("\\."); // 特殊字符分割转义##########################
				if (ss.length >= 4) {
					ip = ss[0] + "." + ss[1] + "." + ss[2] + ".1"; // 因为主机往往是 尾号
					// 寻找当前ip的主机
				}
			}
		}
		return ip;
	}

	public static String cutName(String str) {
		return cutName(str, 4);
	}

	public static String cutName(String str, int i) {
		return str.length() > i ? str.substring(0, i) + ".." : str;
	}

	// /sdcard/mycc/record/100-101020120120120.amr return amr
	public static String getFileTypeByLocalPath(String localpath) {
		String res = "null";

		if (localpath == null) {
		} else {
			String[] ss = localpath.split("/");
			if (ss.length > 0) {
				localpath = ss[ss.length - 1]; // 0120.amr
				ss = localpath.split("\\.");
				if (ss.length > 0) {
					res = ss[ss.length - 1]; // 0120.amr
				}
			}
		}
		return res;
	}

	public static String getFileNameOnlyByLocalPath(String localpath) {
		String res = "null";

		if (localpath == null) {
		} else {
			String[] ss = localpath.split("/");
			if (ss.length > 0) {
				localpath = ss[ss.length - 1]; // 0120.amr
				res = localpath.substring(0, localpath.lastIndexOf("\\."));

			}
		}
		return res;
	}

	/**
	 *  /sdcard/mycc/record/100-101020120120120.amr return asdfa.amr
	 */
	public static String getFileNameByLocalPath(String localpath) {
		String res = "null";

		if (localpath == null) {
		} else {
			String[] ss = localpath.split("/");
			if (ss.length > 0) {
				res = ss[ss.length - 1]; // 0120.amr
			}
		}
		return res;
	}
	public static int getCount(String[] array, String str){
		if(array == null) return -1;
		if(str == null) return -1;
		for(int i = 0; i < array.length; i++){
			if(array[i].equals(str)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获取对象类型 基本数据类型0 map1 list2
	 */
	public static int getType(Object obj){
		if(obj == null) return 0;
		if(obj instanceof Map) return 1;
		if(obj instanceof List) return 2;
		return 0;
	}
	
}
