package com.walker.socket.server_1.plugin;


import java.util.List;

import org.apache.log4j.Logger;

import com.walker.common.util.Bean;
import com.walker.common.util.ClassUtil;
import com.walker.common.util.FileUtil;
import com.walker.common.util.JsonUtil;
import com.walker.core.database.RedisMgr;
import com.walker.core.scheduler.*;
import com.walker.socket.server_1.*;
import com.walker.socket.server_1.plugin.aop.*;

public class PluginMgr {
	private static Logger log = Logger.getLogger(PluginMgr.class);
	private PluginMgr() {
		init();
	}
	public static  PluginMgr getInstance() {
		return SingletonFactory.instance;
	}
	//单例
	private static class SingletonFactory{
		static PluginMgr instance = new PluginMgr();
	}
//	login:{
//		class:util.plugin.Login,
//		on:false,
//		limit:200,
//	}
	private Bean plugins;			//业务处理类
	private List<Bean> aopsBefore;	//处理之前处理
	private List<Bean> aopsAfter;	//处理之后处理 环绕
@SuppressWarnings("unchecked")
//	class	:	util.aop.SizeFilter,
//	on		:	true,
//	excludes	:	[
//		test
//	],
//	params	:	{
//		size	:	2048,
//	},
	
	void init() {
    	String str = FileUtil.readByLines(ClassLoader.getSystemResource("").getPath() + "plugin.json", null);
		Bean bean = JsonUtil.get(str);
		plugins = ((Bean)bean.get("plugins"));	
		aopsBefore = (List<Bean>)bean.get("before");
		aopsAfter = (List<Bean>)bean.get("after");	
//		插件限流控制初始化

//		初始化定时任务计算qps
		Scheduler sch = SchedulerMgr.getInstance();
		try {
			sch.add(new Task("util.socket.server_1.job.JobQpsMinute", "calc each minute qps job", "0/10 * * * * ?"));
//			sch.add(new Task("util.socket.server_1.job.JobQpsHour", "calc each Hour qps job", "0 0 * * * ?"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * 处理消息
	 * @param msg
	 * @throws SocketException
	 */
	public void doMsg(Msg msg) throws SocketException {
		//before 
		if(this.doBefore(msg)) {
			this.doPlugin(msg);
			msg.setTimeSend(System.currentTimeMillis());
			//after
			this.doAfter(msg);
		}		
	}
	/**
	 * 按类别处理 业务 plugin  存储 加工 发送socket
	 */
	public <T> void doPlugin(Msg msg) throws SocketException {
		String type = msg.getType();
		Bean bean = (Bean) plugins.get(type);
		if(bean == null) {
			throw new SocketException("该插件不存在", type);
		}
		if( ! bean.get("on", true)) {
			throw new SocketException("该插件已经关闭", type);
		}
		int limit = bean.get("limit", 0);
		if(limit > 0) {
			throw new SocketException("该插件限流", type, limit);
		}
		String clz = bean.get("class", "");
		Bean params = bean.get("params", new Bean());
		@SuppressWarnings("unchecked")
		Plugin<T> plugin = (Plugin<T>) ClassUtil.newInstance(clz, params);
		plugin.onData(msg);
	}
	
	/**
	 * 执行插件之前
	 * @param msg
	 */
	public <T> Boolean doBefore(final Msg msg) throws SocketException {
		return doAop(aopsBefore, msg);
	}
	/**
	 * 执行插件之后
	 * @param msg
	 */
	public <T> Boolean doAfter(final Msg msg) throws SocketException {
		return doAop(aopsAfter, msg);
	}
	private <T> Boolean doAop(final List<Bean> aops, final Msg msg) throws SocketException {
		for(Bean bean : aops) {
			if(!bean.get("on", false)) {
//				log.warn(Arrays.toString(new String[]{"过滤器已关闭", bean.toString()}));
			}else {
				@SuppressWarnings("unchecked")
				List<String> excludes = (List<String>) bean.get("excludes");
				if(excludes != null && excludes.contains(msg.getType())) {
//					log.debug(Arrays.toString(new String[]{"aop exclude", bean.toString(), msg.getType()}));
				}else {
//					log.debug(Arrays.toString(new String[]{"aop do", msg.getType(), bean.toString() }));
					Bean params = (Bean) bean.get("params");
					String clz = bean.get("class", "");
					@SuppressWarnings("unchecked")
					Aop<T> aop = (Aop<T>) ClassUtil.newInstance(clz, params);

					if(!aop.doAop(msg)) { //有一个拦截器没通过返回异常
						return false;
					}
				}
			}
			
		}
		return true;
	}


}
