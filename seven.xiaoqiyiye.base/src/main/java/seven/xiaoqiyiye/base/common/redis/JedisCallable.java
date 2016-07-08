package seven.xiaoqiyiye.base.common.redis;
/**
 * Jedis回调获取数据
 * @author linya 2016-01-19
 * @see JedisCache
 */
public interface JedisCallable<T> {

	T call();
	
}