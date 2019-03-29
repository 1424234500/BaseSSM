package util.socket.server_1.job;

import java.util.*;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import redis.clients.jedis.Jedis;
import util.Bean;
import util.TimeUtil;
import util.database.RedisMgr;
import util.database.RedisMgr.Fun;
import util.scheduler.TaskJob;
import util.socket.server_1.plugin.aop.Aop;

public class JobQpsMinute extends TaskJob{
	static Logger log = Logger.getLogger(JobQpsMinute.class); 

	static Bean mapLastCount = new Bean();
	static Bean mapLastCountDeta = new Bean();
	
	static Bean mapLastTimeNet = new Bean();
	static Bean mapLastTimeWait = new Bean();
	static Bean mapLastTimeDo = new Bean();
	static Long timeLast = 0L;
	
	@Override
	public void run() {
		RedisMgr.getInstance().doJedis(new Fun<Object>() {
			
			@Override
			public Object make(Jedis jedis) {
				List<String> keys;

//				obj.incrBy("stat:time:net:" + plugin, detaNet);
//				obj.incrBy("stat:time:wait:" + plugin, detaWait);
//				obj.incrBy("stat:time:do:" + plugin, detaDo);
//				obj.incrBy("stat:count:" + plugin, 1L);		//计数 任务pqs
				Long temp = System.currentTimeMillis();
				Long detaTime = (temp - timeLast + 1) / 1000;
				String timeStr = TimeUtil.getTime("yyyy-MM-dd HH:mm:ss");

				//时间区间detaTime60s，次数增长count200次，按接口计算调用qps
				keys = new ArrayList<String>(jedis.keys("stat:count:*"));
				Collections.sort(keys);
				for(String key : keys) {
					String plugin = key.substring(key.lastIndexOf(":") + 1);
					Long before = mapLastCount.get(plugin, 0L);
					Long after = Long.parseLong(jedis.get(key));
					Long count = after - before;
					mapLastCountDeta.set(plugin, count);
					if(count > 0 && detaTime > 0) {
						Long qps = count / detaTime;
						jedis.lpush("stat:qps:" + ":" + plugin, timeStr + " " + qps);
						log.warn("stat:qps:" + ":" + plugin + " " + timeStr + " " + qps);
					}
					mapLastCount.set(plugin, after);
				}
				//时间区间detaTime60s， 耗时增长cost4000， 调用次数count20， 按接口计算调用net wait do次数平均耗时
				keys = new ArrayList<String>(jedis.keys("stat:time:net:*"));
				Collections.sort(keys);
				for(String key : keys) {
					String plugin = key.substring(key.lastIndexOf(":") + 1);
					Long before = mapLastTimeNet.get(plugin, 0L);
					Long after = Long.parseLong(jedis.get(key));
					Long cost = after - before;
					Long count = mapLastCountDeta.get(plugin, 0L);
					if(cost > 0 && count > 0) {
						Long ave = cost / count;
						jedis.lpush("stat:ave:net" + ":" + plugin, timeStr + " " + ave);
						log.warn("stat:ave:net" + ":" + plugin + " " + timeStr + " " + ave);
					}
					mapLastTimeNet.set(plugin, after);
				}

				
				timeLast = temp;

				return null;
			}

		});
		
	}
	
}
