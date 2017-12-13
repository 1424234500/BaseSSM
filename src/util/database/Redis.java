package util.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import util.MapListHelp;
import util.Tools;
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Redis extends Jedis {
	public static void main(String[] args) { 
		
		Redis redis = Redis.getInstance();  

//		redis.set("key", "test set/get key value");
//		
//		String listKey = "list_test";
//		redis.listPush(listKey, 1, 2, 3);
//		List<String> list = redis.listGet(listKey, 0, 10000); 
//		Tools.out(listKey,list);
//		
//		String mapKey = "map_test";
//		redis.mapSet(mapKey, MapListHelp.testSOMap());
//		Map map = redis.mapGet(mapKey);
//		Tools.out(mapKey, map);
//		
//		String setKey = "set_test";
//		redis.setAdd(setKey, MapListHelp.testSSMap());
//		Set set = redis.setGet(setKey );
//		Tools.out(setKey, set);
//		
//		String zsetKey = "zset_test";
//		redis.zsetAdd(zsetKey, MapListHelp.testSDMap());
//		Set zset = redis.zsetGet(zsetKey, 0, 2);
//		Tools.out(zsetKey, zset);
//		 
//		redis.setList("id", MapListHelp.testList());
//		Tools.out(redis.getList());
		
		redis.show();
		redis.showHash();
	} 

	//hash string-string键值对//////////////////////////////////////////
	
	public String mapSet(String key, Map<String, Object> map){ 
		return this.hmset(key, MapListHelp.map2ssmap(map)); 
	}
	public Map<String, Object> mapGet(String key){
		return MapListHelp.map2map(this.hgetAll(key));
	}
	
	//list<string> 类似于stack栈 可重复//////////////////////////////////////////
	
	public long listPush(String key, Object... values){
		this.lpush(key, Tools.objects2strings(values));
		return this.llen(key);
	}
	public long listPop(String key){
		this.lpop(key); 
		return this.llen(key);
	}
	public List<String> listGet(String key, long start, long end){
		return this.lrange(key, start, end); 
	}
	
	//zset 且每个member绑定一个double score->序  不可重复//////////////////////////////////////////

	public long zsetAdd(String key, Map<String, Double> map){
		return this.zadd(key,  (map)); 
	} 
	public Set<String> zsetGet(String key, long start, long end) {
		return this.zrange(key, start, end);
	}
	//set 不可重复//////////////////////////////////////////

	public long setAdd(String key, Object...members){
		return this.sadd(key, Tools.objects2strings(members)); 
	}
	public Set<String> setGet(String key) {
		return this.smembers(key) ;
	}
	
	/**
	 * Redis数据结构 ---- 数据库结构[ id:01, name: walker, age: 18 ]
	 * set get key - value
	 * 1.	id:01 - {name:walker, age:18}
	 * 2.	id:01:name - walker
	 * 2.	id:01:age  - 18
	 * 3.	id:01 -  map{
	 * 						id:01,
	 * 						name:walker,
	 * 						age:18
	 * 					} 
	 */
	/**
	 * 存储所有 添加到redis的map的key值集合
	 */
	private Set<String> keys;
	public Set<String> getKeys(){
		return keys;
	}
	public void clearKeys(){
		for(String key : keys){
			this.del(key);
		}
		keys.clear();
	}
	/**
	 * 添加一个指定key/id的map
	 */
	public String setMap(String key, Map<String, String> map){
		keys.add(key);
		return this.hmset(key, (map)); 
	}
	/**
	 * 移除一个指定key 
	 */
	public long removeMap(String key){
		keys.remove(key);
		return this.del(key);
	}
	/**
	 * 获取指定key的map 
	 */
	public Map<String, String> getMap(String key){
		return  this.hgetAll(key);
	}
	/**
	* 添加一个list，指定list中的map的keyName/id列值为键值
	*/
	public void setList(String keyName, List<Map> list){ 
		for(int i = 0; i < list.size(); i++){
			setMap(MapListHelp.getList(list, i, keyName), list.get(i));
		}  
	}
	/**
	 * 获取所有存储的map  list<map>
	 */
	public List getList(){
		List res = new ArrayList();
		for(String key : keys){
			res.add(this.hgetAll(key));
		}
		return res;
	}
	
	public boolean existsMap(String key){
		return super.exists(key);
	}
	
	
	/**
	 * 显示redis所有数据
	 */
	public void show(){
		Tools.out("-----------Redis show-----------------");

		//获取所有key 各种类型
		Set<String> set = this.keys("*");
		for(String key : set){
			String type = this.type(key);
			Tools.out("key:" + key + ", type:" + type + "  ");
			if(type.equals("string")){
				Tools.out(this.get(key));
			}else if(type.equals("list")){
				Tools.out(this.lrange(key, 0, -1));
			}else if(type.equals("hash")){
				Tools.out(this.hgetAll(key));
			}else if(type.equals("set")){
				Tools.out(this.smembers(key));
			}else if(type.equals("zset")){
				Tools.out(this.zrange(key, 0, -1)); 
			}
			Tools.out("#############");
			
		}
		 
		Tools.out("--------------------------------------");
	}
	
	public void showHash(){
		Tools.out("-----------Redis show-----------------");

		//获取所有key 各种类型
		Set<String> set = this.keys("*");
		for(String key : set){
			String type = this.type(key);
			//Tools.out("key:" + key + ", type:" + type + "  "); 
			if(type.equals("hash")){	
				Tools.out(key, this.hgetAll(key));
			}  
			
		}
		 
		Tools.out("--------------------------------------");
	}
	
	
	

	// 连接本地的 Redis 服务 
	//private Jedis jedis;
	private Redis(){
		super("localhost");
		//jedis = new Jedis("localhost");
		keys = new HashSet<String>();
	}
	
	private static Redis instance; 
	public static  Redis getInstance() {
		if (instance == null) {
			instance = new Redis();
		}
		return instance;
	}
	 
}
