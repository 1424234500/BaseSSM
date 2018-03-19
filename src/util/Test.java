package util;
/**
 * 如何写出无法维护的代码
 *
 */
public class Test {

	public static void main(String[] args) {
		new Test();
	}
	
	Test(){
		defineWord();
		
		
		
	}

	int arr = 0;	//全局命名重用为私有
	void defineWord(){
		int arr = 1;
		
		int α = 1;
		Tools.out(α);
		
		long l = 10l, ll = 101l, li1l = 1011L;
		String mnnrnvuw;
		
		int O = 0;
		int a = 1;/* main 
		int b = 2; * loop
		int c = 3; */
		int d = 4;

		isValid(-1);
		
	}
	boolean isValid(int x){
		 /* 给 i 加 1 */
		//但是永远不要记录包或者方法的整体设计这样的干货。
		//只解释一个程序功能的细节，而不是它要完成的任务是什么。
		//如果你怀疑某个类里可能有bug，天知地知你知就好
		//永远不要对变量声明加注释。有关变量使用的方式、边界值、合法值、小数点后的位数、计量单位、显示格式、数据录入规则等等，后继者完全可以自己从程序代码中去理解和整理嘛
//		避免使用 assert() 机制	
//		为了提高效率，不要使用封装。方法的调用者需要所有能得到的外部信息，以便了解方法的内部是如何工作的。
//		把八进制数混到十进制数列表里，就像这样：
		int arr[] = {111, 120, 013, 121};
//		C编译器会把 myArray[i] 转换成 *(myArray + i)，它等同于 *(i + myArray) 也等同于 i[myArray]。 	
//		在非异常条件下才要使用异常。比如终止循环就可以用 ArrayIndexOutOfBoundsException。还可以从异常里的方法返回标准的结果。
		int re=0;
		try {
			re = s_um(10);
		} catch (Exception e) {
 			//e.printStackTrace();
 			re = Tools.parseInt(e.getMessage());
		}
		Tools.out("exception res sum = " ,re);
		for(int man = 0 ; man < 100; man++,man++){
			
		}
		assert(x > 0);
		Tools.out(x);
		return x*2>0?true:false;
	}
	
	int s_um(int i) throws Exception{
		int res = 0;
		int j = 0;
		j=1;
		for(int va=0;va<i;va++){
			res += va;
			if(va == i-1)
				throw new Exception(res + "");
		}
		return -1;
	}
	
	
	
//	FORTRAN
//
//	用 FORTRAN 写所有的代码。如果老板问你为啥，你可以回答说它有很多非常有用的库，你用它可以节约时间。不过，用 FORTRAN 写出可维护代码的概率是 0，所以，要达到不可维护代码编程指南里的要求就容易多了。
//	用 ASM
//
//	把所有的通用工具函数都转成汇编程序。
//	用 QBASIC
//
//	所有重要的库函数都要用 QBASIC 写，然后再写个汇编的封包程序来处理 large 到 medium 的内存模型映射。
//	内联汇编
//
//	在你的代码里混杂一些内联的汇编程序，这样很好玩。这年头几乎没人懂汇编程序了。只要放几行汇编代码就能让维护代码的程序员望而却步。
//	宏汇编调用C
//
//	如果你有个汇编模块被C调用，那就尽可能经常从汇编模块再去调用C，即使只是出于微不足道的用途，另外要充分利用 goto, bcc 和其他炫目的汇编秘籍。
	
//源码和编译jar包代码不一致！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
//定义常量覆盖专用词语
//	定义一个叫 TRUE 的静态常量。在这种情况下，其他程序员更有可能怀疑你干的不是好事，因为Java里已经有了内建的标识符 true。	
	
//未使用变量i=i
	
	
}
