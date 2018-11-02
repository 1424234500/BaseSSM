package util;

import org.junit.Test;

public class TestString {

	@Test
	public  void testString(){
//java 会确保一个字符串常量只有一个拷贝。
		String s0="kvill"; //常量池 新建
		String s00="kvill"; //常量池  复用 s0 == s00
		
		String s1=new String("kvill"); //堆 新建
		String s2="kv" + new String("ill"); //堆新建 附加   无法在编译期确定 新创建对象”kvill”的应用
		
		
	
		String s3 = "ab";//常量池 新建
		String s4 = "ab" + 1;//常量池 复用  编译器优化 为 'a1'
		String s5 = "a" + "b1";//常量池 复用  编译器优化 为 'a1'
//JVM对于字符串常量的"+"号连接，将程序编译期，JVM就将常量字符串的"+"连接优化为连接后的值，拿"a" + 1来说，经编译器优化后在class中就已经是a1。在编译期其字符串常量的值就确定下来

		String s6 = "a";
		String s7 = s6 + "b"; //!=s3
		final String s8 = "a";
		String s9 = s8 + "b"; //==s3 final 能够确定优化
//		字符串的"+"连接中，有字符串引用存在，而引用的值在程序编译期是无法确定的，即"a" + bb无法被编译器优化，只有在程序运行期来动态分配并将连接后的新地址赋给b
		
		String a = "a";
		String b = "b";
		String c = "c";
		String d = a + b + c; //->编译器自动优化为
		StringBuffer temp = new StringBuffer();
		temp.append(a).append(b).append(c);
		String s = temp.toString();
		
//		immutable性质 不可变类,这一说又要说很多，大家只 要知道String的实例一旦生成就不会再改变了，
//比如说：String str=”kv”+”ill”+” “+”ans”; 就是有4个字符串常量，首先”kv”和”ill”生成了”kvill”存在内存中，
//然后”kvill”又和” ” 生成 “kvill “存在内存中，最后又和生成了”kvill ans”;并把这个字符串的地址赋给了str,
//就是因为String的”不可变”产生了很多临时变量，这也就是为什么建议用StringBuffer的原 因了，因为StringBuffer是可改变的。
		
//什么时候会在常量池存储字符串对象，我想我们可以基本得出结论: 
//1. 显示调用String的intern方法的时候;
//2. 直接声明字符串字面常量的时候，例如: String a = "aaa";
//3. 字符串直接常量相加的时候，例如: String c = "aa" + "bb";  其中的aa/bb只要有任何一个不是字符串字面常量形式，都不会在常量池生成"aabb". 且此时jvm做了优化，不//   会同时生成"aa"和"bb"在字符串常量池中

String temp1 = "hh".intern();
s1 = ("a" + temp1).intern();
s2 = "ahh";
System.out.println(s1 == s2);    // true

s1 = new String("1");    // 同时会生成堆中的对象 以及常量池中1的对象，但是此时s1是指向堆中的对象的
s1.intern();            // 常量池中的已经存在
s2 = "1";
System.out.println(s1 == s2);    // false

String s33 = new String("1") + new String("1");    // 此时生成了四个对象 常量池中的"1" + 2个堆中的"1" + s3指向的堆中的对象（注此时常量池不会生成"11"）
s33.intern();    // jdk1.7之后，常量池不仅仅可以存储对象，还可以存储对象的引用，会直接将s3的地址存储在常量池
String s44 = "11";    // jdk1.7之后，常量池中的地址其实就是s3的地址
System.out.println(s33 == s44); // jdk1.7之前false， jdk1.7之后true

s3 = new String("2") + new String("2");
s4 = "22";        // 常量池中不存在22，所以会新开辟一个存储22对象的常量池地址
s3.intern();    // 常量池22的地址和s3的地址不同
System.out.println(s3 == s4); // false
		
		
	}
}
