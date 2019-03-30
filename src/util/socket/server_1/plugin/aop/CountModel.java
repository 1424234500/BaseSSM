package util.socket.server_1.plugin.aop;

import redis.clients.jedis.Jedis;
import util.database.RedisMgr;
import util.database.RedisMgr.Fun;
import util.socket.server_1.Msg;

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
 * 1.开大netty线程,停止消费处理,增加客户端发送量,观测netty读取socket 存入redis的qps极限
 * 		1	
 * 
 */
public class CountModel {
	//time_client - 网络传输耗时 - time_receive - 队列等待耗时 - time_do - 业务处理耗时 - time_send
	
	public void onNet(final Msg msg) {
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
