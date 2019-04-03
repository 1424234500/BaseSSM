package com.walker.socket.server_1.plugin.aop;

import com.walker.core.database.RedisMgr;
import com.walker.core.database.RedisMgr.Fun;
import com.walker.socket.server_1.Msg;

import redis.clients.jedis.Jedis;

/**
 * 监控 计数 频率qps 发送等待耗时等数据
 * 
 * 1.socket读取到msg时,存入队列之前		统计网络耗时,上行qps		net
 * 2.队列消费时,处理之前				统计排队耗时,消费qps		wait
 * 3.msg处理完毕之后					统计处理耗时,下行qps		do
 *
 * 计数+1 分布式原子操作
 * 
 * 
 * 1.开大netty线程,停止消费处理,停止写入socket,增加客户端发送量,观测netty读取socket 存入redis的qps极限
 * 	netty连接线程数	客户端连接数	客户端发送qps/wait	瓶颈
 * 	1/8				20-160		0-6800/4 			无法模拟更多客户端 failed to create a child event loop		
 * 2.开启消费处理,停止写入socket
 * 	netty	处理线程	客户端连接数	客户端发送qps/wait	排队消费qps/wait	瓶颈
 *  4		1		160			3600/10				1700/32	
 *  							5000/爆炸			600 /爆炸		单机cpu100%分配偏向
 *  				0			0					5000/爆炸		1线程单独消费5000
 *  		4		120			2400/18				2400/80			消费性能足够
 *  				160			3000/250			2000/爆炸
 *  				0			0					8000/爆炸		4线程单独消费8000
 * 3.开启写入socket,完整模拟
 * 	netty	处理线程	客户端连接数	客户端发送qps/wait	排队消费qps/wait	处理写入qps/wait	瓶颈
 * 	4		1 d		160			3400/16				900/爆炸			900/10
 * 					50			1000/2				60/out			60/15
 * 					n			100n				d*1000/20n		1000/20n		假设发送耗时20ms 计算线程数和连接数的关系 
 * 					2			200/0				200/23			200/1	
 * 					11			800/2000							300-400/2
 * 			8
 * 					11			1000/30								900/6
 * 					100			1000/250							220/22
 */
public class CountModel {
	//time_client - 网络传输耗时 - time_receive - 队列等待耗时 - time_do - 业务处理耗时 - time_send
	
	public void onNet(final Msg msg) {
		msg.setTimeReceive(System.currentTimeMillis());

//		onType(msg, "net");
		RedisMgr.getInstance().doJedis(new Fun<Long>() {
			@Override
			public Long make(Jedis obj) {
				String plugin = msg.getType();
				
				//网络传输耗时, 上行socket读取计数 qps
				long detaNet = msg.getTimeReceive() - msg.getTimeClient();
				obj.incrBy("stat:time:net:" + plugin, detaNet);
				obj.incrBy("stat:count:net:" + plugin, 1L);

				return 0l;
			}
		});
	}
	public void onWait(final Msg msg) {
		msg.setTimeDo(System.currentTimeMillis());

//		onType(msg, "wait");
		RedisMgr.getInstance().doJedis(new Fun<Long>() {
			@Override
			public Long make(Jedis obj) {
				String plugin = msg.getType();

				//队列等待耗时, 队列消费计数 qps
				long detaWait = msg.getTimeDo() - msg.getTimeReceive();
				obj.incrBy("stat:count:wait:" + plugin, 1L);
				obj.incrBy("stat:time:wait:" + plugin, detaWait);

				return 0l;
			}
		});
	}
	public void onDone(final Msg msg) {
		msg.setTimeSend(System.currentTimeMillis());

		//		onType(msg, "done");
		RedisMgr.getInstance().doJedis(new Fun<Long>() {
			@Override
			public Long make(Jedis obj) {
				String plugin = msg.getType();

				//业务处理存入socket耗时, 下行计数 qps
				long detaDone = msg.getTimeSend() - msg.getTimeDo();
				obj.incrBy("stat:count:done:" + plugin, 1L);
				obj.incrBy("stat:time:done:" + plugin, detaDone);

				return 0l;
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void onType(final Msg msg) {
//		log.debug(TimeUtil.getTime(msg.getTimeClient(), "yyyy-MM-dd HH:mm:ss:SSS"));
//		log.debug(TimeUtil.getTime(msg.getTimeReceive(), "yyyy-MM-dd HH:mm:ss:SSS"));
//		log.debug(TimeUtil.getTime(msg.getTimeDo(), "yyyy-MM-dd HH:mm:ss:SSS"));
//		log.debug(TimeUtil.getTime(msg.getTimeSend(), "yyyy-MM-dd HH:mm:ss:SSS"));
		RedisMgr.getInstance().doJedis(new Fun<Long>() {
			@Override
			public Long make(Jedis obj) {
				String plugin = msg.getType();
				
				//网络传输耗时, 上行socket读取计数 qps
				long detaNet = msg.getTimeReceive() - msg.getTimeClient();
				obj.incrBy("stat:time:net:" + plugin, detaNet);
				obj.incrBy("stat:count:net:" + plugin, 1L);

				//队列等待耗时, 队列消费计数 qps
				long detaWait = msg.getTimeDo() - msg.getTimeReceive();
				obj.incrBy("stat:count:wait:" + plugin, 1L);
				obj.incrBy("stat:time:wait:" + plugin, detaWait);

				//业务处理存入socket耗时, 下行计数 qps
				long detaDone = msg.getTimeSend() - msg.getTimeDo();
				obj.incrBy("stat:count:done:" + plugin, 1L);
				obj.incrBy("stat:time:done:" + plugin, detaDone);
				return 0l;
			}
		});
	}

	
	private CountModel() {
	}
	public static  CountModel getInstance() {
		return SingletonFactory.instance;
	}
	private static class SingletonFactory{
		static CountModel instance = new CountModel();
	}
	
}
