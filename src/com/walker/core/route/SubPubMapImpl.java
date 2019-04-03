package com.walker.core.route;

import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.Logger;

public class SubPubMapImpl<T> implements SubPub<T>{
//	private ExecutorService pool;
	private Map<String, Set<OnSubscribe<T>>> subscribeTable;
	private static Logger log = Logger.getLogger(SubPubMapImpl.class); 

	
	@Override
	public Boolean publish(String channel, final T object) {
		Set<OnSubscribe<T>> list = subscribeTable.get(channel);
		if(list == null) {
			log.warn("No subscriber channel: " + channel);
		}else {
			for(final OnSubscribe<T> onSub : list) {
				onSub.onSubscribe(object);
//				pool.submit(new Runnable() {
//					public void run() {
//						onSub.onSubscribe(object);
//					}
//				});
			}
		}

		return true;
	}

	@Override
	public Boolean subscribe(String channel, OnSubscribe<T> onSubscribe) {
		if(onSubscribe == null) {
			log.error("No onSubscribe callback channel: " + channel);
			return false;
		}
		Set<OnSubscribe<T>> list = subscribeTable.get(channel);
		if(list == null) {
			list = new CopyOnWriteArraySet<>();
			subscribeTable.put(channel, list);
		}
		Boolean res = list.add(onSubscribe);
		log.info("subscribe " + onSubscribe.toString() + " " + channel + " " + res);
		return res;
	}

	@Override
	public Boolean unSubscribe(String channel, OnSubscribe<T> onSubscribe) {
		Set<OnSubscribe<T>> list = subscribeTable.get(channel);
		if(list == null) {
			log.error("unsubscribe of null?");
			return false;
		}

		Boolean res = list.remove(onSubscribe);
		log.info("unSubscribe " + onSubscribe.toString() + " " + channel + " " + res);
		return res;
	}
	@Override
	public void init(Integer threadSize) {
//		pool = Executors.newFixedThreadPool(threadSize);	
//        pool = new ThreadPoolExecutor(1, threadSize, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        subscribeTable = new ConcurrentHashMap<>();
	}


}
