package util;

import java.util.Date;
import java.util.UUID;

/**
 * 对象类型转换工具
 *
 */
public class LangUtil {

	/**
	 * 目标类型转换
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T turn(Object obj, T defaultValue) {
		T res = null;
		if (obj == null) {
			res = defaultValue;
		} else {
			if (defaultValue instanceof Integer) {
				res = (T) (to(obj, (Integer) defaultValue));
			} else if (defaultValue instanceof Long) {
				res = (T) (to(obj, (Long) defaultValue));
			} else if (defaultValue instanceof Double) {
				res = (T) (to(obj, (Double) defaultValue));
			} else if (defaultValue instanceof Boolean) {
				res = (T) (to(obj, (Boolean) defaultValue));
			} else if (defaultValue instanceof String) {
				res = (T) (obj.toString());
			} else {
				res = (T) obj;
			}
		}
		return res;
	}
	/**
	 * 唯一id生成
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "_");
		uuid = hexTo64(uuid);
		return uuid;
	}

	/**
	 *  下面代码用于将36位的UUID字符串转为22位的字符串，提升系统运行效率
	 */
	private static final char[] CHAR_MAP;
	static {
		CHAR_MAP = new char[64];
		for (int i = 0; i < 10; i++) {
			CHAR_MAP[i] = (char) ('0' + i);
		}
		for (int i = 10; i < 36; i++) {
			CHAR_MAP[i] = (char) ('a' + i - 10);
		}
		for (int i = 36; i < 62; i++) {
			CHAR_MAP[i] = (char) ('A' + i - 36);
		}
		CHAR_MAP[62] = '_';
		CHAR_MAP[63] = '-';
	}

	/**
	 * 将16进制字符串转换为64进制
	 * 
	 * @param hex
	 *            16进制字符串
	 * @return 64进制字符串
	 */
	private static String hexTo64(String hex) {
		StringBuilder r = new StringBuilder();
		int index = 0;
		final int size = 3;
		int[] buff = new int[size];
		int l = hex.length();
		for (int i = 0; i < l; i++) {
			index = i % size;
			buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
			if (index == 2) {
				r.append(CHAR_MAP[buff[0] << 2 | buff[1] >>> 2]);
				r.append(CHAR_MAP[(buff[1] & size) << 4 | buff[2]]);
			}
		}
		return r.toString();
	}

	/**
	 * 转型为整型
	 * 
	 * @param obj
	 *            原始对象
	 * @param def
	 *            缺省值
	 * 
	 * @return 整型
	 */
	public static Integer to(Object obj, int def) {
		if (obj != null) {
			if (obj instanceof Integer) {
				return (Integer) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else if (obj instanceof Boolean) {
				return (Boolean) obj ? 1 : 0;
			} else if (obj instanceof Date) {
				return (int) ((Date) obj).getTime();
			} else {
				try {
					return Integer.parseInt(obj.toString());
				} catch (Exception e) {
					try {
						return (new Double(obj.toString()).intValue());
					} catch (Exception e2) {
						return def;
					}
				}
			}
		} else {
			return def;
		}
	}

	/**
	 * 转型为长整型
	 * 
	 * @param obj
	 *            原始对象
	 * @param def
	 *            缺省值
	 * 
	 * @return 长整型
	 */
	public static Long to(Object obj, long def) {
		if (obj != null) {
			if (obj instanceof Long) {
				return (Long) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof Boolean) {
				return (Boolean) obj ? 1L : 0L;
			} else if (obj instanceof Date) {
				return ((Date) obj).getTime() * 1L;
			} else {
				try {
					return Long.parseLong(obj.toString());
				} catch (Exception e) {
					try {
						return (new Double(obj.toString())).longValue();
					} catch (Exception e2) {
						return def;
					}
				}
			}
		} else {
			return def;
		}
	}

	/**
	 * 转型为双精度浮点型
	 * 
	 * @param obj
	 *            原始对象
	 * @param def
	 *            缺省值
	 * 
	 * @return 双精度浮点型
	 */
	public static Double to(Object obj, Double def) {
		if (obj != null) {
			if (obj instanceof Double) {
				return (Double) obj;
			} else if (obj instanceof Float) {
				return ((Float) obj).doubleValue();
			} else if (obj instanceof Number) {
				return ((Number) obj).doubleValue();
			} else if (obj instanceof Boolean) {
				return (Boolean) obj ? 1d : 0d;
			} else if (obj instanceof Date) {
				return ((Date) obj).getTime() * 1.0;
			} else {
				try {
					return new Double(obj.toString());
				} catch (Exception e) {
					return def;
				}
			}
		} else {
			return def;
		}
	}

	/**
	 * 转型为布尔值
	 * 
	 * @param obj
	 *            原始对象
	 * @param def
	 *            缺省值
	 * 
	 * @return 布尔值
	 */
	public static Boolean to(Object obj, Boolean def) {
		if (obj != null) {
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			} else if (obj instanceof Integer) {
				return ((Integer) obj).intValue() == 1;
			} else if (obj instanceof Long) {
				return ((Long) obj).longValue() == 1;
			} else if (obj instanceof Double) {
				return ((Double) obj).doubleValue() == 1;
			} else if (obj instanceof Date) {
				return ((Date) obj).getTime() == 1;
			} else {
				String str = obj.toString().toUpperCase();
				return str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("YES") || str.equals("1");
			}
		} else {
			return def;
		}
	}

}
