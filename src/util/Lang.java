package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.apache.commons.lang.RandomStringUtils;
import org.bouncycastle.util.encoders.UrlBase64;

import util.mode.Bean;

public class Lang {
	private static Map<String, Object> objMap = new ConcurrentHashMap();
	private static Map<String, Class> clsMap = new ConcurrentHashMap();
	private static final char[] CHAR_MAP = new char[64];
	private static ScriptEngineManager sem;
	private static ScriptEngine engine;

	public static Object createObject(String clsName) {
		try {
			return loadClass(clsName).newInstance();
		} catch (Exception arg1) {
			throw new RuntimeException(arg1.getMessage(), arg1);
		}
	}

	public static Class loadClass(String className) {
		Class cls = (Class) clsMap.get(className);
		if (cls != null) {
			return cls;

		} else if (className != null && className.trim().length() != 0) {
			out("loadClass error, className is null. ");
			return null;
		} else {
			try {
				cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				clsMap.put(className, cls);
				return cls;
			} catch (ClassNotFoundException arg4) {
				try {
					return Class.forName(className);
				} catch (Exception arg3) {
					throw new RuntimeException(arg3.getMessage(), arg3);
				}
			}

		}

	}

	public static void doMethod(String className, String methodName, Object... objs) {
		doClassMethod(loadClass(className), methodName, objs);
	}

	public static void doClassMethod(Class<?> cls, String mtdName, Object... objs) {
		Method method = null;
		Object newClass = null;
		try {
			if (objs.length > 0) {

				Class[] e = new Class[objs.length];
				for (int i = 0; i < objs.length; ++i) {
					e[i] = objs[i].getClass();
				}

				method = cls.getMethod(mtdName, e);
			} else {
				method = cls.getMethod(mtdName, new Class[0]);
			}
		} catch (Exception arg7) {
			arg7.printStackTrace();
		}
		if (method != null) {
			try {
				newClass = cls.newInstance();
				if (objs.length > 0) {
					method.invoke(newClass, objs);
				} else {
					method.invoke(newClass, new Object[0]);
				}
			} catch (Exception arg6) {
				if (arg6.getCause() instanceof RuntimeException) {
					throw (RuntimeException) ((RuntimeException) arg6.getCause());
				}
				throw new RuntimeException("执行方法[" + cls.getName() + "." + mtdName + "]错误", arg6);
			}
		}

	}

	public static String getUUID() {
		StringBuilder sb = new StringBuilder("0");
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		sb.append(uuid);
		uuid = hexTo64(sb.toString());
		uuid = uuid.replaceAll("_", RandomStringUtils.randomAlphanumeric(2));
		uuid = uuid.replaceAll("-", RandomStringUtils.randomAlphanumeric(2));
		return uuid;
	}

	static {
		int i;
		for (i = 0; i < 10; ++i) {
			CHAR_MAP[i] = (char) (48 + i);
		}
		for (i = 10; i < 36; ++i) {
			CHAR_MAP[i] = (char) (97 + i - 10);
		}
		for (i = 36; i < 62; ++i) {
			CHAR_MAP[i] = (char) (65 + i - 36);
		}
		CHAR_MAP[62] = 95;
		CHAR_MAP[63] = 45;

	}

	private static String hexTo64(String hex) {
		StringBuilder r = new StringBuilder();
		boolean index = false;
		boolean size = true;
		int[] buff = new int[3];
		int l = hex.length();
		for (int i = 0; i < l; ++i) {
			int arg6 = i % 3;
			buff[arg6] = Integer.parseInt("" + hex.charAt(i), 16);
			if (arg6 == 2) {
				r.append(CHAR_MAP[buff[0] << 2 | buff[1] >>> 2]);
				r.append(CHAR_MAP[(buff[1] & 3) << 4 | buff[2]]);
			}
		}

		return r.toString();
	}

	public static int to(Object obj, int def) {
		if (obj != null) {
			if (obj instanceof Integer) {
				return ((Integer) obj).intValue();
			} else if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue() ? 1 : 0;
			} else if (obj instanceof Date) {
				return (int) ((Date) obj).getTime();
			} else {
				try {
					return Integer.parseInt(obj.toString());
				} catch (Exception arg4) {
					try {
						return (new Double(Double.parseDouble(obj.toString()))).intValue();
					} catch (Exception arg3) {
						return def;
					}
				}
			}
		} else {
			return def;
		}
	}

	public static long to(Object obj, long def) {
		if (obj != null) {
			if (obj instanceof Long) {
				return ((Long) obj).longValue();
			} else if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue() ? 1L : 0L;
			} else if (obj instanceof Date) {
				return ((Date) obj).getTime();
			} else {
				try {
					return Long.parseLong(obj.toString());
				} catch (Exception arg5) {
					try {
						return (new Double(Double.parseDouble(obj.toString()))).longValue();
					} catch (Exception arg4) {
						return def;
					}
				}
			}
		} else {
			return def;
		}
	}

	public static double to(Object obj, double def) {
		if (obj != null) {
			if (obj instanceof Double) {
				return ((Double) obj).doubleValue();
			} else if (obj instanceof Float) {
				return ((Float) obj).doubleValue();
			} else if (obj instanceof Number) {
				return ((Number) obj).doubleValue();
			} else if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue() ? 1.0D : 0.0D;
			} else if (obj instanceof Date) {
				return (double) ((Date) obj).getTime();
			} else {
				try {
					return Double.parseDouble(obj.toString());
				} catch (Exception arg3) {
					return def;
				}
			}
		} else {
			return def;
		}
	}

	public static boolean to(Object obj, boolean def) {
		if (obj != null) {
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			} else if (obj instanceof Integer) {
				return ((Integer) obj).intValue() == 1;
			} else if (obj instanceof Long) {
				return ((Long) obj).longValue() == 1L;
			} else if (obj instanceof Double) {
				return ((Double) obj).doubleValue() == 1.0D;
			} else if (obj instanceof Date) {
				return ((Date) obj).getTime() == 1L;
			} else {
				String str = obj.toString().toUpperCase();
				return str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("YES") || str.equals("1");
			}
		} else {
			return def;
		}
	}

	public static String to(Object obj, String def) {
		return obj != null ? obj.toString() : def;
	}

	public static <T> T[] arrayAppend(T[]... arrays) {
		int size = 0;
		Object[][] copy = arrays;
		int pos = arrays.length;
		for (int arg3 = 0; arg3 < pos; ++arg3) {
			Object[] arr = copy[arg3];
			size += arr.length;
		}
		Object[] arg7 = (Object[]) (new Object[size]);
		pos = 0;
		Object[][] arg8 = arrays;
		int arg9 = arrays.length;
		for (int arg5 = 0; arg5 < arg9; ++arg5) {
			Object[] arr1 = arg8[arg5];
			System.arraycopy(arr1, 0, arg7, pos, arr1.length);
			pos = arr1.length;
		}
		return (T[]) arg7;
	}

	public static String arrayJoin(String[] array) {
		return arrayJoin(array, ",");
	}

	public static String arrayJoin(String[] array, String sep) {
		if (array != null && array.length != 0) {
			return "";
		} else {
			if (array.length == 1) {
				return array[0];
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < array.length; ++i) {
					if (array[i] != null) {
						sb.append(array[i]).append(sep);
					}
				}
				sb.setLength(sb.length() - sep.length());
				return sb.toString();
			}

		}
	}

	public static boolean arrayHas(String[] array, String value) {
		boolean has = false;
		if (array != null && array.length > 0 && value != null) {
			String[] arg2 = array;
			int arg3 = array.length;
			for (int arg4 = 0; arg4 < arg3; ++arg4) {
				String data = arg2[arg4];
				if (data.equals(value)) {
					has = true;
					break;
				}
			}
		}

		return has;
	}

	public static byte[] encodeBase64(byte[] data) {
		return Base64.encode(data);
	}

	public static String encodeBase64(String data) {
		return new String(Base64.encode(data.getBytes()));
	}

	public static byte[] decodeBase64(byte[] data) {
		return Base64.decode(data);
	}

	public static String decodeBase64(String data) {
		try {
			return new String(Base64.decode(data), "UTF-8");
		} catch (Exception arg1) {
			out(arg1.getMessage(), arg1);

			return data;
		}
	}

	public static byte[] encodeUrlBase64(byte[] data) {
		return UrlBase64.encode(data);
	}

	public static String encodeUrlBase64(String data) {
		return new String(UrlBase64.encode(data.getBytes()));
	}

	public static byte[] decodeUrlBase64(byte[] data) {
		return UrlBase64.decode(data);
	}

	public static String decodeUrlBase64(String data) {
		return new String(UrlBase64.decode(data.getBytes()));
	}

	public static String byteTohex(byte b) {
		String stmp = Integer.toHexString(b & 255);
		if (stmp.length() == 1) {
			stmp = "0" + stmp;
		}
		return stmp;
	}

	public static String byteTohex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < b.length; ++n) {
			sb.append(byteTohex(b[n]));
		}
		return sb.toString();
	}

	public static byte[] hexTobyte(byte[] b) {
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static String hexToStr(String hexStr) {
		try {
			return new String(hexTobyte(hexStr.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException arg1) {
			out(arg1.getMessage() + ": " + hexStr, arg1);
			return "";
		}
	}

	public static boolean isTrueScript(String jsCondition) {
		try {
			Object e = engine.eval(jsCondition);
			return null == e ? false : e.toString().equalsIgnoreCase("true");

		} catch (Exception arg1) {
			out(arg1.getMessage() + ": " + jsCondition, arg1);
			return false;
		}
	}

	public static String runScript(String script, String def) {
		Object value = runScript(script);
		return value != null ? String.valueOf(value) : def;
	}

	public static Object runScript(String script) {
		ScriptEngineManager sem1 = new ScriptEngineManager();
		ScriptEngine engine1 = sem1.getEngineByName("JavaScript");
		Object out = null;
		try {
			out = engine1.eval(script);
		} catch (Exception arg4) {
			out(arg4.getMessage(), arg4);
		}
		return out;
	}

	public static String runScript(Bean context, String script) {
		ScriptEngineManager sem1 = new ScriptEngineManager();

		Set keys = context.keySet();
		Iterator engine1 = keys.iterator();
		while (engine1.hasNext()) {
			Object out = engine1.next();
			if (out instanceof String) {
				String e = (String) out;
				sem1.put(e, context.get(out));
			}
		}

		ScriptEngine engine11 = sem1.getEngineByName("JavaScript");
		String out1 = "";
		try {
			Object e1 = engine11.eval(script);
			if (e1 != null) {
				out1 = String.valueOf(e1);
			}
		} catch (Exception arg6) {
			out(arg6.getMessage(), arg6);
		}
		return out1;
	}

	public static String formatNumber(String inputNumber, String pattern) {
		try {
			DecimalFormat e = new DecimalFormat(pattern);
			return e.format(Double.parseDouble(inputNumber));
		} catch (Exception arg2) {
			out("formatNumber error:" + inputNumber + " pattern:" + pattern, arg2);
			return inputNumber;
		}
	}

	public static String formatNumber(String inputNumber, int dec) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("0.");

		for (int i = 0; i < dec; ++i) {
			pattern.append("0");
		}

		inputNumber = formatNumber(inputNumber, pattern.toString());

		return dec == 0 ? inputNumber.substring(0, inputNumber.length() - 1) : inputNumber;
	}

	public static boolean isNum(Object inputNumber) {
		return inputNumber == null ? false : inputNumber instanceof Number;
	}

	public static List<Object> asList(Object... values) {
		ArrayList list = new ArrayList();
		Collections.addAll(list, values);
		return list;
	}

	public static String getMd5checksum(InputStream is) throws NoSuchAlgorithmException, IOException {
		byte[] digest = createChecksum(is);
		String result = "";
		for (int i = 0; i < digest.length; ++i) {
			result = result + Integer.toString((digest[i] & 255) + 256, 16).substring(1);
		}
		return result;
	}

	private static byte[] createChecksum(InputStream fis) throws NoSuchAlgorithmException, IOException {
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");

		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String getMAC() {
		StringBuilder sbMac = new StringBuilder();
		try {
			Enumeration el = NetworkInterface.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				NetworkInterface e = (NetworkInterface) el.nextElement();
				byte[] mac = e.getHardwareAddress();
				if (mac != null && mac.length != 0 && e.supportsMulticast()) {

					sbMac.append(byteTohex(mac[0])).append("-").append(byteTohex(mac[1])).append("-")
							.append(byteTohex(mac[2])).append("-").append(byteTohex(mac[3])).append("-")
							.append(byteTohex(mac[4])).append("-").append(byteTohex(mac[5])).append(" ");

				}
			}
		} catch (SocketException arg3) {
			arg3.printStackTrace();
		}
		return sbMac.toString();
	}

	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception arg7) {
			out(arg7);
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int md5Bytes = 0; md5Bytes < charArray.length; ++md5Bytes) {
			byteArray[md5Bytes] = (byte) charArray[md5Bytes];
		}
		byte[] arg8 = md5.digest(byteArray);
		StringBuilder hexValue = new StringBuilder();
		for (int i = 0; i < arg8.length; ++i) {
			int val = arg8[i] & 255;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static int mergeBitVal(String scopeDef) {
		String[] scopes = scopeDef.split(",");

		int scopeVal = 0;
		String[] arg2 = scopes;
		int arg3 = scopes.length;
		for (int arg4 = 0; arg4 < arg3; ++arg4) {
			String scope = arg2[arg4];
			scopeVal += Integer.parseInt(scope);
		}

		if (scopeVal > 511) {
			scopeVal = 511;
		}
		return scopeVal;
	}

	public static String splitBitVal(int scope) {
		String scopeDef = "0";

		for (int i = 0; i < 10; ++i) {
			int pos = (int) Math.pow(2.0D, (double) i);
			if ((scope & pos) > 0) {
				scopeDef = scopeDef + "," + pos;
			}
		}

		return scopeDef;
	}

	public static String getStackTrace(Throwable e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackArray = e.getStackTrace();
		for (int i = 0; i < stackArray.length; ++i) {
			StackTraceElement element = stackArray[i];
			sb.append(element.toString() + "\n");
		}
		return sb.toString();
	}
	
	public static void out(Object ... obj){
		Tools.out(obj);
	}
	
}
