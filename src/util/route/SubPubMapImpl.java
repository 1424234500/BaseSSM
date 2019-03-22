package util.route;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

public class SubPubMapImpl<T> implements SubPub<T>{
//	private ExecutorService pool;
	private Map<String, List<OnSubscribe<T>>> subscribeTable;
	private static Logger log = Logger.getLogger("SubPubMap"); 

	
	@Override
	public Boolean publish(String channel, final T object) {
		List<OnSubscribe<T>> list = subscribeTable.get(channel);
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
		List<OnSubscribe<T>> list = subscribeTable.get(channel);
		if(list == null) {
			list = new CopyOnWriteArrayList<>();
			subscribeTable.put(channel, list);
		}
		return list.add(onSubscribe);
	}

	@Override
	public Boolean unSubscribe(String channel, OnSubscribe<T> onSubscribe) {
		List<OnSubscribe<T>> list = subscribeTable.get(channel);
		if(list == null) {
			list = new CopyOnWriteArrayList<>();
			subscribeTable.put(channel, list);
		}
		return list.remove(onSubscribe);
	}
	@Override
	public void init(Integer threadSize) {
//		pool = Executors.newFixedThreadPool(threadSize);	
//        pool = new ThreadPoolExecutor(1, threadSize, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        subscribeTable = new ConcurrentHashMap<>();
	}


}
