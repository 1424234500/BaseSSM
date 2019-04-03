package com.walker.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * 数组 集合 工具类 仿造 java.util.Arrays
 * 测试 Set List Quene Map of Hash Linked LinkedHash 集合 数组  Collection Iterator
 * @author Walker
 * 
 * 
 * 
 * Collection 接口 表示一组对象 add remove clear contains isEmpty size
 * 1.Set	实现非重复
 * 2.List	实现可重复
 * 
 * 
 */
public class ArraysUtil {
	/**
	 * 测试array关键问题
	 * @param argv
	 */
	public static void main(String[] argv){
		Set<String> arr1 = new LinkedHashSet<>();
		arr1.add("s1");
		Collections.addAll(arr1, "s2", "s3");
		Collections.addAll(arr1, new String[]{"s4", "s5"});
		Tools.out("make arr1 ", arr1);
		
		List<String> arr2 = new ArrayList<>();
		arr2.add("s1");
		Collections.addAll(arr2, "s2", "s3");
		Collections.addAll(arr2, new String[]{"s6", "s7"});
		Tools.out("make arr2 ", arr2);
		Collections.shuffle(arr2, new Random(22)); //扰乱
		Tools.out("shuffle乱序 arr2 ", arr2);

		Collections.rotate(arr2, 3); //大循环移位
		Tools.out("rotate小循环移位 arr2 ", arr2);
		
		
		Tools.out("toArray", arr2.toArray(new String[0])); //小于长度 则分配足够的长度  大于长度 则后续置null
		Collection<String> arr3 = new LinkedList<>(arr2);
		arr3.remove("s2");
		arr3.remove("s3");
		Tools.out("make arr3 ", arr3);

		Tools.out("差集 arr1.removeAll(arr2) arr1 - arr2 ", arr1.removeAll(arr2), arr1);
		Tools.out("合集 arr1.addAll(arr2) arr1 ∪ arr2 ", arr1.addAll(arr2), arr1);
		Tools.out("交集 arr1.retainAll(arr2) arr1 ∩ arr2", arr1.retainAll(arr2), arr1);

		List<Map> listBase = new ArrayList<>();
		List<HashMap> list = new ArrayList<>();
		list.add(new HashMap<>());
		listBase.add(new HashMap()); //上转 传入子类 放入基类容器
		//下转 传入 基类容器 和 子类实例  ?号暂时保留类型 传递 关键时刻 上转基类
		Collections.addAll( listBase, new HashMap<>()); //Collection<? super T> c, T... elements 只能把T放到T的父类集合中
		
		
		Iterator<String> it = arr1.iterator();
		StringBuilder sb = new StringBuilder("["); //迭代时不能删改元素！
		while(it.hasNext()) {
		   sb.append(" ").append(it.next());
		}
		sb.append("]");
		Tools.out("iterator ", sb);

		//数组创建
		Integer a[][] = {
				{1,2,3},
				{4,5,6}
		};
		
		Integer b[][] = new Integer[a.length][];
		System.arraycopy(a, 0, b, 0, a.length); //系统浅复制
		Tools.out(Arrays.deepToString(b));
		Tools.out(b);
		
		List<Integer[]> listInte = Arrays.asList(a); //asList获取到 的 List是底层实现 保留 不可改变特性
		Tools.formatOut(listInte);
//		listInte.add(new Integer[]{7, 8, 9}); //异常
		String[] sss = {"a","b"};
		List<String> stooges = Arrays.asList("Larry", "Moe", "Curly");
		arr1.addAll(Arrays.asList(sss));
		arr1.add("bb"); //异常

		Collections.addAll(arr1, "aaa","bbb");
		Tools.out();
		arr1.add("aa"); //异常
		Tools.formatOut(arr1);
		
		TreeMap<String, Object> tm = new TreeMap<>();
		tm.put("aa", "asdkfjadf"); //key 必须实现 comparable 排序需要

		LinkedHashMap<Integer, String> lhm = new LinkedHashMap<>(16, 0.5f, true); //LRU 排序 
		for(int i = 0; i < 5; i++)
			lhm.put(i, "v" + i);
		Tools.out(lhm);
		Random ran = new Random(23);
		for(int i = 0; i < 10; i++){
			lhm.get(ran.nextInt()%lhm.size());
		}
		Tools.out("LRU", lhm);

		
	}
	 
	

	/**
	 * 生成无重复乱序序列
	 */
	public static int[] getSequence  (int size, int start){
		Integer[] res = new Integer[size]; 
		res = initIntArr(res, -1);

		List<Integer> vv = initList(res);
		
		
		int v = 0;
		for(int i = 0;i < res.length; i++){
			v = getRandomNum(vv.size());
			res[i] = vv.get(v);
			vv.remove(v);
		}
		
		System.out.println("start." + start + " size." + size + "  生成序列结果：");
		arrayMake(res, new IMake() {
			public <T> void fun(T[] arr, int i, T value) {
				if(i < 30)
					System.out.print(value + " ");
			}
		});
		System.out.println("");
		
		int[] rr = toInt(res);
		return rr;
	}
	public static int[] toInt(Integer[] arr){
		final int[] res = new int[arr.length];
		arrayMake(arr, new IMake() {
			public <T> void fun(T[] arr, int i, T value) {
				res[i] = (Integer) value;
			}
		});
		return res;
	}
	public static <T> void arrayMake(T arr[], IMake make){
		int len = arr.length;
		for(int i = 0; i < len; i++){
			make.fun(arr, i, arr[i]);
		}
	}
	public interface IMake{
		public <T> void fun(T arr[], int i, T value);
	}
	public static <T> List<T> initList(T[] arr){
		final List<T> res = new ArrayList<T>();
		for(int i = 0; i < arr.length; i++){
			res.add(arr[i]);
		}
//		for(int i = 0 ; i < arr.length; i++){
//			System.out.print(arr[i] + " ");
//		}
		return res;
	}
	public static boolean ifExist(int[] arr, int value){
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == value){
				return true;
			}
		}
		return false;
	}
	public static <T> boolean ifExist(T[] arr, T value){
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == value){
				return true;
			}
		}
		return false;
	}

	public static int[] initIntArr(int[] arr, int value){
		if(value == -1){
			for(int i = 0; i < arr.length; i++){
				arr[i] = i;
			}
			return arr;
		}
		for(int i = 0; i < arr.length; i++){
			arr[i] = value;
		}
		return arr;
	}
	public static Integer[] initIntArr(Integer[] arr, Integer value){
		if(value == -1){
			for(int i = 0; i < arr.length; i++){
				arr[i] = i;
			}
			return arr;
		}
		for(int i = 0; i < arr.length; i++){
			arr[i] = value;
		}
		return arr;
	}
	public static int getRandomNum(int size){
		return (int) (Math.random() * size);
	}
   
	
	
	
	
	
}
