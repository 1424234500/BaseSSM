package com.walker.common.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.walker.common.util.ThreadUtil;
import com.walker.common.util.Tools;
import com.walker.common.util.ThreadUtil.Type;

/**
 * 地铁站用车 消费者 生产者模型
 *
 */

public class ThreadSyncCreatorCoster {
	int num = 0; // 资源数量
	Lock lock = new ReentrantLock();	//3.lock
	int dd = 1;				//每片间隔
	int maxdd = 60 / dd;	//最大分片
	int sleep = 1200 / 5 *  dd;	//线程延时
	int ff = 10;	//缩进
	public ThreadSyncCreatorCoster() {
		startCreator();
		startCoster();
	}
	//计算整形模拟分布
	public int gau(int x){
		double xd = ((1d * x - maxdd / 2) / maxdd * 4 );
		return (int) (100*gau(xd));
	}
	//标准正态分布
	public double gau(double x){
		int cegma = 1;
		int ue = 0;
		double y = 0;
		y = (1.0 / (Math.sqrt(2*Math.PI) * cegma) ) * Math.exp( - Math.pow((x-ue), 2) / (2 * Math.pow(cegma,2)) );
//		Tools.out(x,y);
		return y;
	} 
	// 开启生产者
	public void startCreator() {
		ThreadUtil.execute(Type.DefaultThread, new Runnable() {
			public void run() {
				int tt = 0;
				while (true) {
					tt++;
					int make = gau(tt + maxdd / 5);
					Tools.out("08:" + tt * dd, "生产" + make, Tools.fillStringBy("", "+", make/ff, 0));
					lock.lock();
					try{
						num += make;
					}finally{
						lock.unlock();
					}							
					Tools.out(Tools.fillStringBy("", "=", num/ff, 0));
					ThreadUtil.sleep(sleep);
					if (tt > maxdd)
						break;
				}
			}
		});
	}

	// 开启消费者
	public void startCoster() {
		ThreadUtil.execute(Type.DefaultThread, new Runnable() {
			public void run() {
				int tt = 0;
				while (true) {
					tt++;
					int cost = gau(tt - maxdd / 5);
					lock.lock();
					try{
						if(num >= cost){
							num -= cost;
							Tools.out("08:" + tt * dd, "消费" + cost, Tools.fillStringBy("", "-", cost/ff, 0));
						}else{
							Tools.out("08:" + tt * dd, "失败" + cost, Tools.fillStringBy("", "-", cost/ff, 0));
						}
					}finally{
						lock.unlock();
					}					
					Tools.out(Tools.fillStringBy("", "=", num/ff, 0));
					ThreadUtil.sleep(sleep);
					if (tt > maxdd)
						break;
				}
			}
		});
	}


}
