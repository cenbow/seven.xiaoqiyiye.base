package seven.xiaoqiyiye.base.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * 提供keySerializer，在{@code RedisTemplate} 中将默认的{@code JdkSerializationRedisSerializer}替换掉，
 * JdkSerializationRedisSerializer作为keySerializer，会存在key之前有16进制数据作为前缀的问题。
 * @author linya
 * @see RedisTemplate#setKeySerializer(org.springframework.data.redis.serializer.RedisSerializer)
 * @see JedisCacheFactory#newInstance(Class, org.springframework.beans.factory.BeanFactory)
 */
public class ObjectRedisSerializer extends GenericToStringSerializer<Object>{

	public ObjectRedisSerializer() {
		super(Object.class);
	}
	
	public ObjectRedisSerializer(Class<Object> type) {
		super(type);
	}

}
