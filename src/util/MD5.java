package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//MD5加密工具类  
public class MD5 {
	public final static String make(String id, String pwd) {
		return makeStr("cc" + id + pwd);
	}

	public final static String makeKey(String id, long time) {
		return makeStr("cc" + id + time);
	}

	public final static String makeStr(String str) {
		if (null == str) {
			str = "";
		}
		String MD5Str = "";
		try {
			// JDK 6 支持以下6种消息摘要算法，不区分大小写
			// md5,sha(sha-1),md2,sha-256,sha-384,sha-512
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;
			StringBuilder builder = new StringBuilder(32);
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					builder.append("0");
				builder.append(Integer.toHexString(i));
			}
			MD5Str = builder.toString();
			// LogUtil.println("result: " + buf.toString());// 32位的加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return MD5Str;
	}
}