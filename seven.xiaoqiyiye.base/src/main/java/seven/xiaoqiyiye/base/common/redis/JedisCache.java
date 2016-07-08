package seven.xiaoqiyiye.base.common.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 定义Redis的基本操作
 * 
 * 1. 删除key值
 * 2. 对Value、Set、List、Map等数据进行设置
 * 3. 设置过期时间
 * 4. 设置JedisCallable回调
 * 
 * @author linya 2016-01-19
 * 
 */
public class JedisCache<T> {

	private RedisTemplate<String, T> redisTemplate;
	
	private Class<T> valueClass;
	
	protected JedisCache(RedisTemplate<String, T> redisTemplate, Class<T> valueClass){
		this.redisTemplate = redisTemplate;
		this.valueClass = valueClass;
	}
	
	/**
	 * 包装Key
	 * @param key
	 * @return
	 */
	private String wrapKey(String key){
		return valueClass.getSimpleName() + ":" + key;
	}
	
	/**
	 * 包装Key集合
	 * @param keys
	 * @return
	 */
	private Collection<String> wrapKeys(Collection<String> keys){
		if(keys == null){
			return Collections.emptyList();
		}
		List<String> wrapKeys = new ArrayList<String>(keys.size());
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			wrapKeys.add(wrapKey(it.next()));
		}
		return wrapKeys;
	}
	
	/**
	 * 给指定的key设置过期时间
	 * @param key
	 * @param timeout
	 * @param unit
	 */
	private void expire(String key, long timeout, TimeUnit unit){
		redisTemplate.expire(wrapKey(key), timeout, unit);
	}
	
	/**
	 * 删除指定的key
	 * @param key
	 */
	public void delete(String key){
		redisTemplate.delete(wrapKey(key));
	}
	
	/**
	 * 删除指定的key集合
	 * @param keys
	 */
	public void delete(Collection<String> keys){
		redisTemplate.delete(wrapKeys(keys));
	}
	
	/**
	 * 设置单值数据
	 * @param key
	 * @param value
	 */
	public void set(String key, T value){
		redisTemplate.opsForValue().set(wrapKey(key), value);
	}
	
	/**
	 * 设置单值数据，并设置过期时间
	 * @param key
	 * @param value
	 * @param timeout
	 * @param unit
	 */
	public void set(String key, T value, long timeout, TimeUnit unit){
		set(key, value);
		expire(key, timeout, unit);
	}
	
	/**
	 * 设置Set数据
	 * @param key
	 * @param sets
	 */
	@SuppressWarnings("unchecked")
	public void set(String key, Set<T> sets){
		SetOperations<String, T> setOperation = redisTemplate.opsForSet();
		String wrapKey = wrapKey(key);
		for(T t: sets){
			setOperation.add(wrapKey, t);
		}
	}
	
	/**
	 * 设置Set数据，并设置过期时间
	 * @param key
	 * @param sets
	 * @param timeout
	 * @param unit
	 */
	public void set(String key, Set<T> sets, long timeout, TimeUnit unit){
		set(key, sets);
		expire(key, timeout, unit);
	}
	
	/**
	 * 设置List数据
	 * @param key
	 * @param lists
	 */
	public void set(String key, List<T> lists){
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		String wrapKey = wrapKey(key);
		for(T t: lists){
			listOperation.rightPush(wrapKey, t);
		}
	}
	
	/**
	 * 设置List数据，并设置过期时间
	 * @param key
	 * @param lists
	 * @param timeout
	 * @param unit
	 */
	public void set(String key, List<T> lists, long timeout, TimeUnit unit){
		set(key, lists);
		expire(key, timeout, unit);
	}
	
	/**
	 * 设置Map<K,T>数据
	 * @param key
	 * @param maps
	 */
	public <K> void set(String key, Map<K, T> maps){
		HashOperations<String, Object, Object> mapOperation = redisTemplate.opsForHash();
		String wrapKey = wrapKey(key);
		mapOperation.putAll(wrapKey, maps);
	}
	
	/**
	 * 设置Map<K,T>数据，并设置过期时间
	 * @param key
	 * @param maps
	 * @param timeout
	 * @param unit
	 */
	public <K> void set(String key, Map<K, T> maps, long timeout, TimeUnit unit){
		set(key, maps);
		expire(key, timeout, unit);
	}
	
	/**
	 * 获取单值数据
	 * @param key
	 * @return
	 */
	public T get(String key){
		ValueOperations<String, T> setOperation = redisTemplate.opsForValue();
		String wrapKey = wrapKey(key);
		T value = setOperation.get(wrapKey);
		return value;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置数据
	 * @param key
	 * @param callalbe
	 * @return
	 */
	public T get(String key, JedisCallable<T> callalbe){
		T value = get(key);
		if(value != null){
			return value;
		}
		value = callalbe.call();
		if(value != null){
			set(key, value);
		}
		return value;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置过期时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @param callalbe
	 * @return
	 */
	public T get(String key, long timeout, TimeUnit unit, JedisCallable<T> callalbe){
		T value = get(key, callalbe);
		expire(key, timeout, unit);
		return value;
	}
	
	/**
	 * 获取Set数据
	 * @param key
	 * @return
	 */
	public Set<T> getSet(String key){
		SetOperations<String, T> setOperation = redisTemplate.opsForSet();
		String wrapKey = wrapKey(key);
		Set<T> sets = setOperation.members(wrapKey);
		return sets;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置数据
	 * @param key
	 * @param callable
	 * @return
	 */
	public Set<T> getSet(String key, JedisCallable<Set<T>> callable){
		Set<T> sets = getSet(key);
		if(sets != null){
			return sets;
		}
		sets = callable.call();
		if(sets != null){
			set(key, sets);
		}
		return sets;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置过期时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @param callalbe
	 * @return
	 */
	public Set<T> getSet(String key, long timeout, TimeUnit unit, JedisCallable<Set<T>> callalbe){
		Set<T> sets = getSet(key, callalbe);
		expire(key, timeout, unit);
		return sets;
	}
	
	/**
	 * 获取List数据
	 * @param key
	 * @return
	 */
	public List<T> getList(String key){
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		String wrapKey = wrapKey(key);
		List<T> lists = listOperation.range(wrapKey, 0, -1);
		return lists;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置数据
	 * @param key
	 * @param callable
	 * @return
	 */
	public List<T> getList(String key, JedisCallable<List<T>> callable){
		List<T> lists = getList(key);
		if(lists != null){
			return lists;
		}
		lists = callable.call();
		if(lists != null){
			set(key, lists);
		}
		return lists;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置过期时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @param callalbe
	 * @return
	 */
	public List<T> getList(String key, long timeout, TimeUnit unit, JedisCallable<List<T>> callalbe){
		List<T> lists = getList(key, callalbe);
		expire(key, timeout, unit);
		return lists;
	}	
	
	/**
	 * 获取存储在redis中的Map数据
	 * keyClass存在泛型察除问题，所以一定要与保存数据时的Map<K,V>的key类型一致
	 * @param key
	 * @param keyClass Map<K,V>中的K类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <K> Map<K, T> getMap(String key, Class<K> keyClass){
		HashOperations<String, Object, Object> mapOperation = redisTemplate.opsForHash();
		String wrapKey = wrapKey(key);
		Map<Object, Object> cacheMaps = mapOperation.entries(wrapKey);
		Map<K, T> maps = new HashMap<K, T>();
		for(Map.Entry<Object, Object> entry: cacheMaps.entrySet()){
			maps.put((K) entry.getKey(), (T)entry.getValue());
		}
		return maps;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置数据
	 * @param key
	 * @param keyClass
	 * @param callable
	 * @return
	 */
	public <K> Map<K, T> getMap(String key, Class<K> keyClass, JedisCallable<Map<K, T>> callable){
		Map<K, T> maps = getMap(key, keyClass);
		if(maps != null){
			return maps;
		}
		maps = callable.call();
		if(maps != null){
			set(key, maps);
		}
		return maps;
	}
	
	/**
	 * 如果不存在，则通过JedisCallable回调获取，并设置过期时间
	 * @param key
	 * @param keyClass
	 * @param timeout
	 * @param unit
	 * @param callalbe
	 * @return
	 */
	public <K> Map<K, T> getMap(String key, Class<K> keyClass, long timeout, TimeUnit unit, JedisCallable<Map<K, T>> callalbe){
		Map<K, T> maps = getMap(key, keyClass, callalbe);
		expire(key, timeout, unit);
		return maps;
	}	
	
	public ValueOperations<String, T> opsForValue(){
		return redisTemplate.opsForValue();
	}
	
}