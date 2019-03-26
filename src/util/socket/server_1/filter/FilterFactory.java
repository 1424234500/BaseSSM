package util.socket.server_1.filter;


import java.util.*;

import org.apache.log4j.Logger;

import util.Bean;
import util.ClassUtil;
import util.socket.server_1.*;
import util.socket.server_1.session.*;

public class FilterFactory {
	private static Logger log = Logger.getLogger(FilterFactory.class); 

	private static List<Bean> filters;
	
//	class	:	util.filter.SizeFilter,
//	on		:	true,
//	excludes	:	[
//		test
//	],
//	params	:	{
//		size	:	2048,
//	},
//	
	public static <T> Boolean doFilter(Session<T> session, MsgUp msg) throws SocketException {
		for(Bean bean : filters) {
			if(!bean.get("on", false)) {
//				log.warn(Arrays.toString(new String[]{"过滤器已关闭", bean.toString()}));
			}else {
				@SuppressWarnings("unchecked")
				List<String> excludes = (List<String>) bean.get("excludes");
				if(excludes != null && excludes.contains(msg.getType())) {
					log.info(Arrays.toString(new String[]{"例外", bean.toString(), msg.getType()}));
				}else {
					log.info(Arrays.toString(new String[]{"拦截", bean.toString(), msg.getType()}));
					Bean params = (Bean) bean.get("params");
					String clz = bean.get("class", "");
					Filter<T> filter = getFilter(clz, params);
					if(!filter.doFilter(session, msg)) { //有一个过滤没通过返回异常
						return false;
					}
				}
			}
			
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> Filter<T> getFilter(String clz, Bean params) throws SocketException {
		Filter<T> filter = (Filter<T>) ClassUtil.newInstance(clz, params);
		return filter;
	}
	public static void init(List<Bean> filters) {
		FilterFactory.filters = filters;
	}

}
