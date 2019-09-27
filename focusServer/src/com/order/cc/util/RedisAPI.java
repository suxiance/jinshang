package com.order.cc.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 *@author zn
 *@time 2017-11-20-下午2:19:39
 *@description:
 */
public class RedisAPI {
	//Redis对象连接池
	private static  JedisPool jedisPool;
	public static JedisPool getJedisPool() {
		return jedisPool;
	}
	@Autowired
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	public static Jedis getJedis() {
		Jedis jedis = jedisPool.getResource();
		jedis.select(5);
		return jedis;
	}
	/********************* start: 对list的操作   ************************/
	public static boolean lpush(String key, Object object) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.lpush(key.getBytes(),SerializeUtil.serialize(object));
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
	/**
	 * 取出最早进入的对象
	 * @param key
	 * @return
	 */
	public static synchronized Object rget(String key) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			Long num = jedis.llen(key);
			byte[] bytes = jedis.lindex(key.getBytes(), num-1);
			return SerializeUtil.unserialize(bytes);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return null;
	}
	
	/**
	 * 右侧弹出，即先进先出
	 * @param key
	 * @return
	 */
	public static Object rpop(String key) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			byte[] bytes = jedis.rpop(key.getBytes());
			return SerializeUtil.unserialize(bytes);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return null;
	}
	
	/**
	 * 获取名为key的list的长度（比如key为userQueue时，返回用户排队中的排队个数）
	 * @param key
	 * @return
	 */
	public static Long llen(String key) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			Long len = jedis.llen(key);
			return len;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return (long) 0;
	}
	
	/**
	 * 删除名为key的list中的object元素
	 * @param key
	 * @param object
	 * @return
	 */
	public static boolean lrem(String key, Object object) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.lrem(key.getBytes(), 1, SerializeUtil.serialize(object));
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
	
	public static LinkedList<Object> lrange(String key) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			
			Long len = jedis.llen(key);
			List<byte[]> list = jedis.lrange(key.getBytes(), 0, len);
			LinkedList<Object> objList = new LinkedList<Object>();
		    for (int i = 0; i < list.size(); i++) {
			   byte[] objects = list.get(i);
			   Object obj = SerializeUtil.unserialize(objects);
			   objList.add(obj);
		    }
			return objList;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return null;
	}
	/********************* end: 对list的操作   ************************/
	
	/********************* start: 对hash的操作   ************************/
	public static boolean hset(String key, String field, String value) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.hset(key, field, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
		
	}
	
	public static boolean hdel(String key, String field) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.hdel(key, field);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
	public static synchronized boolean hincrBy(String key, String field, long value){
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.hincrBy(key, field, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
	public static synchronized boolean hexists(String key,String value){
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.hexists(key, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
	public static synchronized String hget(String key,String field) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedisPool, jedis);
        }
        return value;
    }
	
	public static Map<String, String> hgetAll(String key) {
		Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            value = jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedisPool, jedis);
        }
        return value;
	}
	/********************* end: 对hash的操作   ************************/
	
	/********************* start: 对字符串操作   ************************/
	public static synchronized boolean set(String key,String value){
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.set(key, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
    public static synchronized String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedisPool, jedis);
        }
        return value;
    }
    /********************* end: 对字符串操作   ************************/
    
    /********************* start: redis通用操作   ************************/
	@SuppressWarnings("deprecation")
	public static void returnResource(JedisPool jedisPool,Jedis jedis){
		if(jedis != null){
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
     * 判断某个key是否存在
     * @param key
     * @return
     */
    public static synchronized boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedisPool, jedis);
        }
        return false;
    }
	
	public static Set<String> keys(String key) {
		Set<String> keys = null;
		Jedis jedis = null;
        try {
        	jedis = getJedis();
        	keys = jedis.keys(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedisPool, jedis);
        }
        return keys;
	}
	
	public static boolean del(String key) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.del(key);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	
	public static boolean flushall() {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.flushAll();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	/*设置建的生存时间*/
	public static boolean expire(String key,int seconds) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			jedis.expire(key, seconds);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return false;
	}
	/*查看建的剩余生存时间*/
	public static Long ttl(String key) {
		Jedis jedis = null;
		try{
			jedis=getJedis();
			Long ttl = jedis.ttl(key);
			return ttl;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnResource(jedisPool, jedis);
		}
		return -2l;
	}
}
