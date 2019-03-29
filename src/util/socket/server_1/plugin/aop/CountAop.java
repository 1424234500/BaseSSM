package util.socket.server_1.plugin.aop;

import redis.clients.jedis.Jedis;
import util.Bean;
import util.TimeUtil;
import util.database.RedisMgr;
import util.database.RedisMgr.Fun;
import util.socket.server_1.Msg;
import util.socket.server_1.session.Session;

/**
 * 监控 计数 频率qps 发送等待耗时
 *
 */
public class CountAop<T> extends Aop<T>{
	CountAop(Bean params) {
		super(params);
	}

	@Override
	public Boolean doAop(final Msg msg) {
		
		RedisMgr.getInstance().doJedis(new Fun<Long>() {
			@Override
			public Long make(Jedis obj) {
				//计数+1 分布式原子操作
				String plugin = msg.getType();
				
//time_client - 网络传输耗时 - time_receive - 队列等待耗时 - time_do - 业务处理耗时 - time_send
				long detaNet = msg.getTimeReceive() - msg.getTimeClient();
				long detaWait = msg.getTimeDo() - msg.getTimeReceive();
				long detaDo = msg.getTimeSend() - msg.getTimeDo();
//				log.debug(TimeUtil.getTime(msg.getTimeClient(), "yyyy-MM-dd HH:mm:ss:SSS"));
//				log.debug(TimeUtil.getTime(msg.getTimeReceive(), "yyyy-MM-dd HH:mm:ss:SSS"));
//				log.debug(TimeUtil.getTime(msg.getTimeDo(), "yyyy-MM-dd HH:mm:ss:SSS"));
//				log.debug(TimeUtil.getTime(msg.getTimeSend(), "yyyy-MM-dd HH:mm:ss:SSS"));
				
				
				obj.incrBy("stat:time:net:" + plugin, detaNet);
				obj.incrBy("stat:time:wait:" + plugin, detaWait);
				obj.incrBy("stat:time:do:" + plugin, detaDo);
				obj.incrBy("stat:count:" + plugin, 1L);		//计数 任务pqs
				
				return 0l;
			}
		});
		
		return true;
	}

}
