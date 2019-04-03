package com.walker.common.demo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关于多线程同步锁
 * 多线程中的非同步问题主要出现在对域的读写上 
 * 用final常量域，有锁保护的域和volatile域可以避免非同步的问题
 * 
 * 1.synchronized	 高开销的操作，因此应该尽量减少同步的内容
 * 锁方法		 锁代码块
 * java的每个对象都有一个内置锁，当用此关键字修饰方法时， 内置锁会保护整个方法
 * 但是不能是抽象方法，也不能是接口中的接口方法
 * 也可以修饰静态方法，此时如果调用该静态方法，将会锁住整个类。
 * 
 * wait 	notify
 * wait 使一个线程处于等待状态，并且释放所持有的对象的lock
 * notify 唤醒一个处于等待状态的线程，注意的是在调用此方法的时候，并不能确切的唤醒某一个等待状态的线程，而是由JVM确定唤醒哪个线程，而且不是按优先级。
 * 
 * 2.volatile
 * a.volatile关键字为域变量的访问提供了一种免锁机制
   b.使用volatile修饰域相当于告诉虚拟机该域可能会被其他线程更新
   c.因此每次使用该域就要重新计算，而不是使用寄存器中的值 
   d.volatile不会提供任何原子操作，它也不能用来修饰final类型的变量 
 * 
 * 3.lock javaSE 5.0 add
 * 与使用synchronized方法和快具有相同的基本行为和语义，并且扩展了其能力。
 *  ReentrantLock() : 创建一个ReentrantLock实例  还有创建公平锁的构造方法，但由于能大幅度降低程序运行效率
	lock() : 获得锁 
	unlock() : 释放锁 
 * 
 * 注：关于Lock对象和synchronized关键字的选择： 
		a.最好两个都不用，使用一种java.util.concurrent包提供的机制，能够帮助用户处理所有与锁相关的代码。 
		b.如果synchronized关键字能满足用户的需求，就用synchronized，因为它能简化代码 
		c.如果需要更高级的功能，就用ReentrantLock类，此时要注意及时释放锁，否则会出现死锁，通常在finally代码释放锁 
 * 
 * 4.ThreadLocal
 * 使用ThreadLocal管理变量，则每一个使用该变量的线程都获得该变量的副本，副本之间相互独立，
 * 这样每一个线程都可以随意修改自己的变量副本，而不会对其他线程产生影响。
 * ThreadLocal() : 创建一个线程本地变量 
	get() : 返回此线程局部变量的当前线程副本中的值 
	initialValue() : 返回此线程局部变量的当前线程的"初始值" 
	set(T value) : 将此线程局部变量的当前线程副本中的值设置为value    
 *     
 * 注：ThreadLocal与同步机制 
		a.ThreadLocal与同步机制都是为了解决多线程中相同变量的访问冲突问题。 
		b.前者采用以"空间换时间"的方法，后者采用以"时间换空间"的方式   
 *     
 * 5.LinkedBlockingQueue<E> 阻塞队列 业务处理 fifo
 * 		LinkedBlockingQueue<Runnable> 方法体阻塞	
 * 		LinkedBlockingQueue<String>   参数阻塞
 * Thread( LinkedBlockingQueue.push) ) 多线程 生产者
 * Thread( while( run(LinkedBlockingQueue.pop) ) ) 线程处理 消费者
 *
 * 6.AtomicInteger  原子操作 	事务
 * 将读取变量值、修改变量值、保存变量值看成一个整体来操作即-这几种行为要么同时完成，要么都不完成。在java的util.concurrent.atomic包中提供了创建了原子类型变量的工具类
 *     
 *     
 *     
 */







public class ThreadSync {
	int money;							//1.syn
	volatile int lastMoney;				//2.volatile
	Lock lock = new ReentrantLock();	//3.lock
	//4.ThreadLocal
	static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
	//6.Atomic 
    AtomicInteger firstMoney = new AtomicInteger(100);		
    
    
	/**
	 * 1.锁方法
	 * 线程在执行同步方法时是具有排它性的。
	 * 当任意一个线程进入到一个对象的任意一个同步方法时，
	 * 这个对象的所有同步方法都被锁定了，在此期间，
	 * 其他任何线程都不能访问这个对象的任意一个同步方法，
	 * 直到这个线程执行完它所调用的同步方法并从中退出，
	 * 从而导致它释放了该对象的同步锁之后。在一个对象被某个线程锁定之后，
	 * 其他线程是可以访问这个对象的所有非同步方法的。
	 */
	public synchronized void addMoney(int deta){
		this.money += deta;
	}
	/**
	 * 1.锁代码块
	 */
	public void addMoney1(int deta){
		synchronized (this) {
			this.money += deta;	
		}
	}
	/**
	 * 2.volatile 无锁
	 */
	public void addMoney2(int deta){
		this.money += deta;
	}
	/**
	 * 3.lock
	 */
	public void addMoney3(int deta){
		lock.lock();
		try{
			this.money += deta;
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 4.ThreadLocal
	 */
	public void addMoney4(int deta){
		threadLocal.set(threadLocal.get() + deta);
	}
	/**
	 * 6.Atomic
	 */
	public void addMoney6(int deta){
		this.firstMoney.addAndGet(deta);
	}
	
	public static void main(String[] argv){
		
		
		
		
	}
	
	
	
	
}
