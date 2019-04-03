package com.walker.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.walker.common.setting.Setting;

//MD5加密工具类  
public class MD5 {
	
	/**
	 * 解密对比工具
	 * 解密库0-9 -> A-Z -> z-z
	 * 12931231923 -> 0-9-A-Z-a-z 进制的编码 ++
	 * 使用多线程协作 线程池队列 分配任务 处理密码比对
	 * 最小长度
	 * 最大长度
	 */
	public static void parse(String key, Integer minLen, Integer maxLen, Fun<String> call){
		
		
		startCosumer();
		startCreator(key, minLen, maxLen, call);
	}
	private static Queue<Bean> quene = new LinkedBlockingQueue<>();
	private static void startCosumer(){
		int i = 0;
		while(i++ < 10){
			final int ii = i;
			new Thread(new Runnable(){
				@SuppressWarnings("unchecked")
				public void run(){
					Tools.out("线程启动 " + ii);
					//把需要处理的值放入缓存队列
					//核心耗时 线程分流处理  编码 加密
					while(!Thread.interrupted()){
						Bean bean = quene.poll();
						if(bean != null){ 
							BigInteger value = (BigInteger) bean.get("value");
							if(value != null){
								int nowLen = bean.get("nowlen", 0);
								char start = bean.get("start", '0');
								char stop  = bean.get("stop", 'z');
								String key  = bean.get("key", "");
								Fun<String> call  = (Fun<String>) bean.get("call");
								
								String pkey = encode(value, nowLen, start, stop);
								String tkey = call.make(pkey); //加密规则暴露
								Tools.out(pkey.equals(key), value, pkey, tkey, key);
								if(pkey.equals(key)){
									Tools.out(value, "res", pkey);
									Setting.saveProperty("rrrrrrrrrrrrrrr", pkey);
								}
							}
						}else{
							ThreadUtil.sleep(10);
						}
					}
					
				}
			}).start();
		}
		
	}
	private static void startCreator(final String key, final Integer minLen, final Integer maxLen, final Fun<String> call){
		ThreadUtil.execute(new Runnable(){
			public void run(){
				Tools.out("生产者启动");
				final char start = '0';
				final char stop = 'z';

				BigInteger base = BigInteger.valueOf(stop - start + 1);
				BigInteger maxValue = BigInteger.ONE;
				for(int i = 0; i < minLen; i++){
					maxValue = maxValue.multiply(base);
				}
				BigInteger value;
				for(int nowLen = minLen; nowLen <= maxLen; nowLen ++){
					
					value = BigInteger.ZERO; //0->max
					while(value.compareTo(maxValue) < 0){
						//把需要处理的值放入缓存队列
						//核心耗时 线程分流处理  编码 加密

						quene.add(new Bean().put("value", value)
								.put("nowlen", nowLen)
								.put("start", start)
								.put("stop", stop)
								.put("key", key)
								.put("call", call)
								);
						while(Thread.interrupted() && quene.size() > 32767)
							ThreadUtil.sleep(10);
						
						value = value.add(BigInteger.ONE);
					}
				}
			}
		});
		
	}
	
	
	/**
	 * 编码 把value编码成N进制 进制基数编码为start->stop 并填充位数len
	 * @param value
	 * @param start
	 * @param stop
	 * @return
	 */
	public static String encode(BigInteger value, int len, char start, char stop){
		assert(stop >= start + 1); //'0' '1' 2进制
		BigInteger cLen = BigInteger.valueOf(stop - start + 1); //23进制 '0' -> '0' + 23
		StringBuilder sb = new StringBuilder();
		
		while(value.compareTo(BigInteger.ZERO) > 0){
//			Tools.out(value, sb.toString());
			sb.append((char)(start +  value.mod(cLen).intValue())) ;//22 -> '0'+22
			value = value.divide(cLen);
		}
		sb.reverse();
		return Tools.fillStringBy(sb.toString(), ""+start, len, 0);
	}
//	1、新建一个值为123的大整数对象
//	BigInteger a=new BigInteger(“123”); //第一种，参数是字符串
//	BigInteger a=BigInteger.valueOf(123); //第二种，参数可以是int、long
//
//	2、大整数的四则运算
//	a. add(b); //a,b均为BigInteger类型，加法
//	a.subtract(b); //减 法
//	a.divide(b); //除法
//	a.multiply(b); //乘法
//
//	3、大整数比较大小
//	a.equals(b); //如果a、b相等返回true否则返回false
//	a.comareTo(b); //a小于b返回-1，等于返回0，大于返回1
//
//	4、常用方法
//	a.mod(b); //求余
//	a.gcd(b); //求最大公约数
//	a.max(b); //求最大值
//	a.min(b); //求最小值
	
	
	
	
	public final static String make(String id, String pwd) {
		return makeStr("cc" + id + pwd);
	}

	public final static String makeKey(String id, long time) {
		return makeStr("cc" + id + time);
	}
	/**
	 * 加密
	 */
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