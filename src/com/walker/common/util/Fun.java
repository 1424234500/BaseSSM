package com.walker.common.util;
/**
 * 通用泛型回调接口
 * 返回值采取协变? 或者 泛型定义
 * @param <T>
 */
public interface Fun<A>{ 
	public <T> T make(A obj) ;
}

