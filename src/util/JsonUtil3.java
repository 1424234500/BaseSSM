package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import util.mode.Bean;

public class JsonUtil3 {
	public static Bean toBean(JSONObject jsObject) {
		Bean bean = new Bean();

		try {
			String key;
			Object value;
			for (Iterator e = jsObject.keys(); e.hasNext(); bean.set(key, value)) {
				key = (String) e.next();
				value = jsObject.get(key);
				if (!(value instanceof JSONArray)) {
					if (value instanceof JSONObject) {
						value = toBean((JSONObject) value);
					} else if (JSONObject.NULL.equals(value)) {
						value = "";
					}
				} else {
					JSONArray tmpArray = (JSONArray) value;
					ArrayList list = new ArrayList(tmpArray.length());
					int index = 0;

					for (int len = tmpArray.length(); index < len; ++index) {
						Object data = tmpArray.get(index);
						if (data instanceof JSONObject) {
							list.add(toBean((JSONObject) data));
						} else {
							list.add(data);
						}
					}

					value = list;
				}
			}

			return bean;
		} catch (Exception arg9) {
			throw new RuntimeException(arg9.getMessage(), arg9);
		}
	}

	public static Bean toBean(String str) {
		Bean bean = null;
		if (str != null && str.length() > 0) {
			try {
				JSONObject e = new JSONObject(str);
				bean = toBean(e);
			} catch (Exception arg2) {
				throw new RuntimeException(arg2.getMessage(), arg2);
			}
		}

		return bean == null ? new Bean() : bean;
	}

	public static List<Bean> toBeanList(String str) {
		ArrayList beanList;
		if (str != null && str.length() > 0) {
			try {
				JSONArray e = new JSONArray(str);
				int len = e.length();
				beanList = new ArrayList(len);

				for (int index = 0; index < len; ++index) {
					beanList.add(toBean(e.getJSONObject(index)));
				}
			} catch (Exception arg4) {
				throw new RuntimeException(arg4.getMessage(), arg4);
			}
		} else {
			beanList = new ArrayList();
		}

		return beanList;
	}

	public static String toJson(Map<?, ?> map) {
		return toJson(map, false);
	}

	public static String mapsToJson(Map... maps) {
		StringBuilder sb = new StringBuilder("{");
		Map[] len = maps;
		int arg2 = maps.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Map map = len[arg3];
			if (!map.isEmpty()) {
				sb.append(toJson(map, false, false, false)).append(",");
			}
		}

		int arg5 = sb.length();
		if (arg5 > 1) {
			sb.setLength(arg5 - 1);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String toJson(Map<?, ?> map, boolean formatFlag) {
		return toJson(map, formatFlag, false);
	}

	public static String toJson(Map<?, ?> map, boolean formatFlag, boolean emptyFlag) {
		return toJson(map, formatFlag, emptyFlag, true);
	}

	public static String toJson(Map<?, ?> map, boolean formatFlag, boolean emptyFlag, boolean withBrace) {
		String sep = formatFlag ? "\r\n" : "";
		StringBuilder sb = new StringBuilder();
		if (withBrace) {
			sb.append("{");
		}

		Iterator len = map.keySet().iterator();

		while (len.hasNext()) {
			Object key = len.next();
			Object value = map.get(key);
			StringBuilder sbLine = new StringBuilder();
			sbLine.append(sep).append("\"").append(key).append("\":");
			if (value != null) {
				 if (!(value instanceof String) && !value.getClass().isPrimitive()) {
					if (value instanceof List) {
						sbLine.append(sep).append(toJson((List) value, formatFlag, emptyFlag));
					} else if (value instanceof Map) {
						sbLine.append(sep).append(toJson((Map) value, formatFlag, emptyFlag));
					} else {
						sbLine.append("\"").append(encode(value.toString())).append("\"");
					}
				} else {
					String var = value.toString();
					if (var.length() == 0) {
						if (emptyFlag) {
							sbLine.setLength(0);
						} else {
							sbLine.append("\"\"");
						}
					} else {
						sbLine.append("\"").append(encode(value.toString())).append("\"");
					}
				}
			} else if (emptyFlag) {
				sbLine.setLength(0);
			} else {
				sbLine.append("\"\"");
			}

			if (sbLine.length() > 0) {
				sb.append(sbLine).append(",");
			}
		}

		int len1 = sb.length();
		if (len1 > 1) {
			sb.setLength(len1 - 1);
		}

		sb.append(sep);
		if (withBrace) {
			sb.append("}");
		}

		return sb.toString();
	}

	public static String toJson(List<?> list) {
		return toJson(list, false);
	}

	public static String toJson(List<?> list, boolean formatFlag) {
		return toJson(list, formatFlag, false);
	}

	public static String toJson(List<?> list, boolean formatFlag, boolean emptyFlag) {
		String sep = formatFlag ? "\r\n" : "";
		StringBuilder sb = new StringBuilder("[");
		Iterator len = list.iterator();

		while (len.hasNext()) {
			Object bean = len.next();
			if (bean != null) {
				if (bean instanceof Map) {
					sb.append(sep).append(toJson((Map) bean, formatFlag, emptyFlag)).append(",");
				} else if (bean instanceof List) {
					sb.append(sep).append(toJson((List) bean, formatFlag, emptyFlag)).append(",");
				} else {
					sb.append(sep).append("\"").append(bean).append("\",");
				}
			}
		}

		int len1 = sb.length();
		if (len1 > 1) {
			sb.setLength(len1 - 1);
		}

		sb.append(sep).append("]");
		return sb.toString();
	}

	public static String toJson(Object obj, boolean formatFlag) {
		if (obj == null) {
			return "";
		} else if (obj instanceof List) {
			return toJson((List) obj, formatFlag);
		} else if (obj instanceof Map) {
			return toJson((Map) obj, formatFlag);
		} else {
			throw new RuntimeException("wrong json ojbect type");
		}
	}

	public static String encode(String s) {
		StringBuffer sb = new StringBuffer();
		int len = s.length();

		for (int i = 0; i < len; ++i) {
			char c = s.charAt(i);
			switch (c) {
				case '\b' :
					sb.append("\\b");
					break;
				case '\t' :
					sb.append("\\t");
					break;
				case '\n' :
					sb.append("\\n");
					break;
				case '\f' :
					sb.append("\\f");
					break;
				case '\r' :
					sb.append("\\r");
					break;
				case '\"' :
					sb.append("\\\"");
					break;
				case '/' :
					sb.append("\\/");
					break;
				case '\\' :
					sb.append("\\\\");
					break;
				default :
					sb.append(c);
			}
		}

		return sb.toString();
	}
	
	 
	
	
}