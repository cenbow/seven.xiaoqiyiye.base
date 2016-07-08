package seven.xiaoqiyiye.base.common.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.core.RedisTemplate;

import seven.xiaoqiyiye.base.util.SpringContextUtils;

/**
 * 获取JedisCache的工厂类
 * @author linya
 */
public class JedisCacheFactory {
	
	private static final Map<Class<?>, JedisCache<?>> jedisCacheTypePool = new HashMap<Class<?>, JedisCache<?>>();
	
	private static BeanFactory beanFactory;
	
	private JedisCacheFactory(){}
	
	@SuppressWarnings("unchecked")
	public static <T> JedisCache<T> newInstance(Class<T> valueClass){
		
		if(beanFactory == null){
			beanFactory = SpringContextUtils.getApplicationContext();
		}
		
		JedisCache<T> jedisCache;
		if(jedisCacheTypePool.containsKey(valueClass)){
			jedisCache = (JedisCache<T>) jedisCacheTypePool.get(valueClass);
		}else{
			RedisTemplate<String, T> redisTemplate = beanFactory.getBean(RedisTemplate.class);
			jedisCache = new JedisCache<T>(redisTemplate, valueClass);
			jedisCacheTypePool.put(valueClass, jedisCache);
		}
		
		return jedisCache;
	}
	
}
