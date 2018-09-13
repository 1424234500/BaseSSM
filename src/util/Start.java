package util;

import java.util.HashMap;

/**
 * 元组 模式 构建 多元素
 * @author walker
 *
 * @param <T>
 */
public class Start<V1, V2>{
	public final V1 v1;
	public final V2 v2;
	
	public Start(V1 v1, V2 v2){
		this.v1 = v1;
		this.v2 = v2;
	}
	
	
	public static Start<?,?> fun(){
		return new Start<String, Bean>("sss", new Bean().put("key", "value"));
	}
	
	public static void test(String[] argv){
		Start<String, HashMap> tu = new Start<>("sss", new HashMap());
		Start<?, ?> tt = fun();
		Tools.out(tt.v1, tt.v2);
	}
	
} 