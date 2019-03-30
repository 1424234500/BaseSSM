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

public class JobQpsMinute extends TaskJob{
	static Logger log = Logger.getLogger(JobQpsMinute.class); 
	static String[] types = {"net", "wait", "done"};
	static Bean mapLastCount = new Bean();
	static Bean mapLastCountDeta = new Bean();
	static Bean mapLastTime = new Bean();
	static {
		for(String type: types) {
			mapLastCount.put(type, new Bean());
			mapLastCountDeta.put(type, new Bean());
			mapLastTime.put(type, new Bean());
		}
	}
	
	static Long timeLast = 0L;
	
	@Override
	public void run() {
		RedisMgr.getInstance().doJedis(new Fun<Object>() {
			
			@Override
			public Object make(Jedis jedis) {
				List<String> keys;

//				obj.incrBy("stat:time:net:" + plugin, detaNet);
//				obj.incrBy("stat:time:wait:" + plugin, detaWait);
//				obj.incrBy("stat:time:done:" + plugin, detaDone);
//				obj.incrBy("stat:count:net" + plugin, 1L);
//				obj.incrBy("stat:count:wait" + plugin, 1L);
//				obj.incrBy("stat:count:done" + plugin, 1L);
				
				//生成qps 
				//stat:qps:net:		+ plugin
				//stat:qps:wait:	+ plugin
				//stat:qps:done:	+ plugin
				//生成ave 平均耗时
				//stat:ave:net:		+ plugin
				//stat:ave:wait:	+ plugin
				//stat:ave:done:	+ plugin
				
				Long temp = System.currentTimeMillis();
				Long detaTime = (temp - timeLast + 1) / 1000;
				String timeStr = TimeUtil.getTime("yyyy-MM-dd HH:mm:ss");

				//时间区间detaTime60s，次数增长count200次，按类型net wait done 按接口message login 计算调用qps
				for(String type : types) {	//net
					Bean mapLastCountItem = (Bean) mapLastCount.get(type);
					Bean mapLastCountDetaItem =  (Bean) mapLastCountDeta.get(type);
					Bean mapLastTimeItem = (Bean) mapLastTime.get(type);

					keys = new ArrayList<String>(jedis.keys("stat:count:" + type + ":*"));
					Collections.sort(keys);
					for(String key : keys) {
						String plugin = key.substring(key.lastIndexOf(":") + 1);	//message
						Long before = mapLastCountItem.get(plugin, 0L);
						Long after = Long.parseLong(jedis.get(key));
						Long count = after - before;
						mapLastCountDetaItem.set(plugin, count);
						if(count > 0 && detaTime > 0) {
							Long qps = count / detaTime;	//stat:qps:net:message
							jedis.lpush("stat:qps:" + type + ":" + plugin, timeStr + " " + qps);
							log.warn("stat:qps:" + type + ":" + plugin + " " + timeStr + " " + qps);
						}
						mapLastCountItem.set(plugin, after);
					}

					//时间区间detaTime60s， 耗时增长cost4000， 调用次数count20， 按接口计算调用net wait do次数平均耗时
					keys = new ArrayList<String>(jedis.keys("stat:time:" + type + ":*"));
					Collections.sort(keys);
					for(String key : keys) {	//stat:time:net:message
						String plugin = key.substring(key.lastIndexOf(":") + 1);	//message
						Long before = mapLastTimeItem.get(plugin, 0L);
						Long after = Long.parseLong(jedis.get(key));
						Long cost = after - before;
						Long count = mapLastCountDetaItem.get(plugin, 0L);
						if(cost > 0 && count > 0) {
							Long ave = cost / count;	//stat:ave:net:message
							jedis.lpush("stat:ave:" + type + ":" + plugin, timeStr + " " + ave);
							log.warn("stat:ave:" + type + ":" + plugin + " " + timeStr + " " + ave);
						}
						mapLastTimeItem.set(plugin, after);
					}
				}
				
				

				

				
				timeLast = temp;

				return null;
			}

		});
		
	}
	
}
