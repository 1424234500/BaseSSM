package com.walker.socket.server_1.job;

import java.util.*;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.walker.common.util.Bean;
import com.walker.common.util.TimeUtil;
import com.walker.common.util.Tools;
import com.walker.core.database.RedisMgr;
import com.walker.core.database.RedisMgr.Fun;
import com.walker.core.scheduler.TaskJob;

import redis.clients.jedis.Jedis;

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
				
//stat:net:message	2019-12-12 n-qps 320  n-ave 29   w-qps 32  w-ave 32  d-qps 31 d-ave
				
				Long temp = System.currentTimeMillis();
				Long detaTime = (temp - timeLast + 1) / 1000;
				String timeStr = TimeUtil.getTime("yyyy-MM-dd HH:mm:ss");
				
				Bean typeBean = new Bean();

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
						detaTime = detaTime <= 0 ? 1 : detaTime;
						Long qps = count / detaTime;	//message	*net:qps:232    
						typeBean.put(plugin, typeBean.get(plugin, "")+ " " + type + " " + fill("qps " + qps));
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
						count = count <= 0 ? 1 : count;
						Long ave = cost / count;	//*net:ave:322ms  
						typeBean.put(plugin, typeBean.get(plugin, "") + " " + fill("ave " + ave));

						mapLastTimeItem.set(plugin, after);
					}
				}
				

				//stat:net:message	2019-12-12 n-qps 320  n-ave 29   w-qps 32  w-ave 32  d-qps 31 d-ave
				for(Object obj : typeBean.keySet()) {
					String plugin = obj.toString();
					String value = typeBean.get(plugin, "");
					jedis.lpush("stat:" + plugin, timeStr + " " + value);
					log.warn("stat:" + plugin + " " + timeStr + " " + value);
				}
				
				timeLast = temp;

				return null;
			}

			private String fill(String str) {
				return Tools.fillStringBy(str, " ", 8, 1);
			}

		});
		
	}
	
}
