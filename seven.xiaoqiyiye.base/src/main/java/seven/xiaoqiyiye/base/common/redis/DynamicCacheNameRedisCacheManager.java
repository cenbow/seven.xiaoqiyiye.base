package seven.xiaoqiyiye.base.common.redis;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * 动态创建RedisCacheManager的缓存名cacheName
 * 
 * 1. 重写RedisCacheManager#setCacheNames(Collection)方法，取消对dynamic=false的设置
 * 2. 如果没有设置cacheNames，默认添加default，解决Spring3.2.0在不配置cacheNames属性时抛出的Empty Collection错误问题
 * 
 * @author linya 2016-01-19
 * @see RedisCacheManager#setCacheNames(Collection)
 */
public class DynamicCacheNameRedisCacheManager extends RedisCacheManager{

	private static final String DEFAUT_CACHE_NAME = "default";
	
	@SuppressWarnings("rawtypes")
	public DynamicCacheNameRedisCacheManager(RedisTemplate template) {
		super(template);
	}

	@Override
	public void setCacheNames(Collection<String> cacheNames) {
		boolean created = false;
		if(cacheNames == null){
			cacheNames = new ArrayList<String>();
			created = true;
		}
		Collection<String> newCacheNames = (created ? cacheNames : new ArrayList<String>(cacheNames));
		if (CollectionUtils.isEmpty(newCacheNames)) {
			newCacheNames.add(DEFAUT_CACHE_NAME);
		}
		for (String cacheName : newCacheNames) {
			createAndAddCache(cacheName);
		}
	}
	
}
