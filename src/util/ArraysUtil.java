package util;

import java.util.*;

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
		Tools.out("toArray", arr2.toArray(new String[0])); //小于长度 则分配足够的长度  大于长度 则后续置null
		Collection<String> arr3 = new LinkedList<>(arr2);
		arr3.remove("s2");
		arr3.remove("s3");
		Tools.out("make arr3 ", arr3);

		Tools.out("差集 arr1.removeAll(arr2) arr1 - arr2 ", arr1.removeAll(arr2), arr1);
		Tools.out("合集 arr1.addAll(arr2) arr1 ∪ arr2 ", arr1.addAll(arr2), arr1);
		Tools.out("交集 arr1.retainAll(arr2) arr1 ∩ arr2", arr1.retainAll(arr2), arr1);

		
		Iterator<String> it = arr1.iterator();
		StringBuilder sb = new StringBuilder("["); //迭代时不能删改元素！
		while(it.hasNext()) {
		   sb.append(" ").append(it.next());
		}
		sb.append("]");
		Tools.out("iterator ", sb);
		
		
		
	}
	 
}
