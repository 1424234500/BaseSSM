package com.walker.common.util;
/**
 * 通用泛型回调接口
 * 返回值采取协变? 或者 泛型定义
 * @param <T>
 */
public interface FunT<A, T>{ 
	public T make(A obj) ;
}

