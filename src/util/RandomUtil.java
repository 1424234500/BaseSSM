package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生成一个整型无重随机序列，无参构造使用数组序列生成，有参构造使用完全剩余系定理（效率高，包含生成质数序列）
 * @author Alvinte
 *
 */
public class RandomUtil {
 
 
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