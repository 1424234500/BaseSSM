package com.walker.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 排序汇总 学习
 * 冒泡 选择 插入 归并 快速
 */
public class SortUtil {

	/**
	 * 排序工具 List<Map> 多字段
	 * 都是List参数的情况 不能自动转换 List<Bean> -> List<Map>  但是可以转换 Bean -> Map
	 * ? extends 的用处
	 */
	public static void sort(final List<? extends Map> list, final boolean ifReverse, final Object...keys){
		Collections.sort(list, new Comparator<Map>() {
			@Override
			public int compare(Map o1, Map o2) {
				int res = 0;
				if(o1 != null && o2 != null){
					for(Object key : keys){
						Object ov1 = o1.get(key);
						Object ov2 = o2.get(key);
						if(ov1 != null && ov2 != null){
							res = ov1.toString().compareTo(ov2.toString());
							if(res != 0){
								break;
							}
						}else{
							res = ov1 == null ? -1:1;
						}
					}
				}else{
					res = o1 == null ? -1 : 1;
				}
				return (ifReverse?1:-1) * res;
			}
		});
	}
	
	/**
	 * Java官方 timSort 排序 
	 */
	public static <T> T[] timSort(T[] arr, Comparator<T> comp){
		logTime();
		countIf = 0;
		countSwap = 0;
		int len = arr.length;
		Arrays.sort(arr, comp);
		showTime("Java timSort \t大小:" + len + " 比较次数:" + countIf + " 交换次数:" + countSwap);
		return arr;
	}
	/**
	 * 冒泡排序 相邻比较交换
	 */
	public static <T> T[] bubbleSort(T[] arr, Comparator<T> comp){
		logTime();
		int len  = arr.length;
		countIf = 0;
		countSwap = 0;
		for(int i = 0; i < len; i++){
			for(int j = 0; j < len - i - 1; j++){
				//第一次冒泡 相邻两两比较 交换   冒出最大值
				if(comp.compare(arr[j], arr[j + 1]) > 0){
					swap(arr, j, j + 1);
				}
			}
		}
		showTime("冒泡排序  bubbleSort \t大小:" + len + " 比较次数:" + countIf + " 交换次数:" + countSwap);
		return arr;
	}
	/**
	 * 选择排序 选择最值交换
	 */
	public static <T> T[] selectionSort(T[] arr, Comparator<T> comp){
		logTime();
		int len  = arr.length;
		countIf = 0;
		countSwap = 0;
		int maxIndex = 0;
		for(int i = 0; i < len - 1; i++){
			maxIndex = 0;
			for(int j = 1; j < len - i; j++){
				//第一次选择 全部比较 选择最大值
				if(comp.compare(arr[maxIndex], arr[j]) < 0){
					maxIndex = j;
				}
			}
			if(maxIndex != len - i - 1){
				swap(arr, maxIndex, len - i - 1);
			}
		}
		showTime("选择排序  selectionSort \t大小:" + len + " 比较次数:" + countIf + " 交换次数:" + countSwap);
		return arr;
	}

	/**
	 * 折半查找 插入排序 局部有序插入
	 */
	public static <T> T[] insertSort(T[] arr, Comparator<T> comp){
		logTime();
		int len  = arr.length;
		countIf = 0;
		countSwap = 0;
		T now;
		//默认第一个为有序 从第二个开始插入
		for(int i = 1; i < len; i++){
			now = arr[i];
			//now 3 -> (0, ?)    
			//now 4 -> (0, 3, 5, 7, 9, ?)
			//				  4 - - 后移
			//获取插入位置 arr, 0, i 查找now位置
			//二分查找
			int k = 0;
			k = search(arr, now, 0, i, comp);
			/*	
		 	//普通查找
			for(k = 0; k < i; k++){	
				countIf ++;
				if(comp.compare(now, arr[k]) < 0){
					break;
				}
			} 
			*/
			//根据插入位置移位 插入
			for(int j = i; j > k; j--){
				arr[j] = arr[j - 1];
			}
			arr[k] = now;
			countSwap += ((i - k)); //赋值次数 3次相当于一次交换
//			Tools.out(arr);
		}
		countSwap /= 3;
		showTime("插入排序  insertSort \t大小:" + len + " 比较次数:" + countIf + " 交换次数:" + countSwap);
		return arr;
	}
	/**
	 * 二分查找 插入位置 正序
	 */
	public static <T> int search(T[] arr, T now, int left, int right, Comparator<T> comp){
		int res = -1;
		int middle = (left + right) / 2;
		if(middle > left){ // 4 5 6  
			if(comp.compare(now, arr[middle]) > 0){ //now > middle -> middle,right
				return search(arr, now, middle, right, comp);
			}else{//now <= middle -> left,middle
				return search(arr, now, left, middle, comp);
			}
		}else{ // 4 4 5
			res = comp.compare(now, arr[left]) > 0 ? right : left;
		}
		return res;
	}
	/**
	 * 快速排序 关键值 都小 都大 分割
	 */
	public static <T> T[] quickSort(T[] arr, Comparator<T> comp){
		logTime();
		int len  = arr.length;
		countIf = 0;
		countSwap = 0;
		qsort(arr, 0, len - 1, comp);
		
		showTime("快速排序  quickSort \t大小:" + len + " 比较次数:" + countIf + " 交换次数:" + countSwap);
		return arr;
	}
	static long countIf = 0;
	static long countSwap = 0;
	/**
	 * 局部快速排序
	 */
	public static <T> T[] qsort(T[] arr, int low, int high, Comparator<T> comp){
		if(low >= high)return arr;
		int first = low;
		int last = high;
		T key = arr[first];
		while(first < last){
			while(first < last){
				if(comp.compare(arr[last], key) > 0){
					last--;
				}else{
					break;
				}
			}
			arr[first] = arr[last];
			while(first < last){
				if(comp.compare(arr[first], key) <= 0){
					first++;
				}else{
					break;
				}
			}
			arr[last] = arr[first];
		}
		countSwap ++;
		arr[first] = key;
//		Tools.out(arr);
		qsort(arr, low, first - 1, comp);
		qsort(arr, first + 1, high, comp);
		
		return arr;
	}
	
	
	
	
	static long time = 0;
	public static void logTime(){
		time = System.currentTimeMillis();
	}
	public static void showTime(String info){
		long nowTime = System.currentTimeMillis();
		long deta = nowTime - time;
		Tools.out(info + " 耗时:" + Tools.calcTime(deta));
	}
    /**
     * 交换元素
     */
	public static <T> void swap(T[] x, int a, int b) {
        T t = x[a];
        x[a] = x[b];
        x[b] = t;
		countSwap ++;
    }
	public static void test(int len){
		Bean[] list = new Bean[len];
		Bean[] res;
		int[] sequence = ArraysUtil.getSequence(len, 0);
		for(int i = 0; i < len; i++){
			list[i] = (Bean.getBean().put("k", sequence[i]));//Tools.getRandomNum(0, len, 0)));
		}
//		Tools.out(list);
		Comparator<Bean> comp = new Comparator<Bean>(){
			@Override
			public int compare(Bean o1, Bean o2) {
				countIf ++;
				return o1.get("k",0).compareTo(o2.get("k",0));
			}
		};
		res = bubbleSort(list.clone(), comp);
//		Tools.out(res);
		res = selectionSort(list.clone(), comp);
		res = insertSort(list.clone(), comp);
//		Tools.out(res);
		res = quickSort(list.clone(), comp);
//		Tools.out(res);
		res = timSort(list.clone(), comp);
		Tools.out(res);
	}
	
	
	
	
	public static void main(String[] argc){
		test(1000);
		test(10000);
//		test(100000);
	}
	
}