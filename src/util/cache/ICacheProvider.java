package util.cache;

import java.util.List;


/**
 * 缓存服务接口类
 * @author Jerry Li
 *
 */
public interface ICacheProvider {
    
    /**
    * @param type 类型
     * @param key 键值
     * @return 对象值
     */
    Object get(String type, String key);
    
    /**
     * @param type 类型
     * @param key 键值
     * @param val 内容
     * @return 是否成功设置
     */
    boolean set(String type, String key, Object val);
    
    /**
     * @param type 类型
     * @param key 键值
     * @param val 内容
     * @param expiry 有效期时间，单位秒
     * @return 是否成功设置
     */
    boolean set(String type, String key, Object val, int expiry);
    
    /**
     * @param type 类型
     * @param key 键值
     * @return 是否成功删除
     */
    boolean delete(String type, String key);
    
    /**
     * 清除此类型下的所有数据
     * @param type 类型
     * @return 是否成功清除
     */
    boolean clear(String type);
    
    /**
     * @param type 类型
     * @param key 键值
     * @return 是否存在
     */
    boolean exists(String type, String key);
    
    /**
     * 关闭缓存
     */
    void shutdown();

    /**
     * 得到此类型下的缓存数量
     * @param type 类型
     * @return 缓存数量
     */
    int getSize(String type);
    
    /**
     * 得到此类型下的键值列表
     * @param type 类型
     * @return 键值列表
     */
    List<String> getKeyList(String type);
    
    /**
     * 获取此类型下的数据列表
     * @param type 类型
     * @param offset 从那个位置开始取，从0开始
     * @param size 取多少条记录
     * @return 数据列表
     */
    List<Object> getValueList(String type, int offset, int size);

    /**
     * 获取实际的缓存服务类
     * @return 实际的缓存服务类
     */
    Object getCacheMgr();
}
