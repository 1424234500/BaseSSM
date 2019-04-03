package com.walker.common.util;

import java.util.HashMap;

/**
 * 元组 模式 构建 多元素返回
 * @author walker
 *
 * @param <T>
 */
public class Tuple<V1, V2>{
	public final V1 v1;
	public final V2 v2;
	
	public Tuple(V1 v1, V2 v2){
		this.v1 = v1;
		this.v2 = v2;
	}
	
	
	public static Tuple<?,?> fun(){
		return new Tuple<String, Bean>("sss", new Bean().put("key", "value"));
	}
	
	public static void test(String[] argv){
		Tuple<String, HashMap> tu = new Tuple<>("sss", new HashMap());
		Tuple<?, ?> tt = fun();
		Tools.out(tt.v1, tt.v2);
	}
	
} 