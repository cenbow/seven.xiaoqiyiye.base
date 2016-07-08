package seven.xiaoqiyiye.base.common.redis;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * 基于fastjson实现Redis缓存的序列化器
 * <p>
 * 提供valueSerializer，在{@code RedisTemplate} 中将默认的{@code JdkSerializationRedisSerializer}替换掉，
 * 采用json形式存储，便于查看缓存内容，便于以后进行维护。
 * </p>
 * <p>
 * <p>
 * FastJsonRedisSerializer 负责将待序列化的对象包装成RedisEntityWrapper对象，所有缓存到Redis中的都是RedisEntityWrapper类型，
 * 该类包装了原生的缓存对象和类型。
 * 序列化过程:
 * 缓存对象进行序列化时，调用serialize(Object t)方法，在方法中将对象进行包装成RedisEntityWrapper，并返回json字符串的字节数组到Redis中。
 * 反序列化过程:
 * 缓存对象反序列化过程通过调用deserialize(byte[] bytes)方法，在该方法中使用{@code RedisEntityWrapper wrapper = JSONObject.parseObject(bytes, RedisEntityWrapper.class);}
 * 来反序列化RedisEntityWrapper对象，由于在ParserConfig全局配置中设置了RedisEntityWrapper.class的反序列化接口，所以会使用RedisEntityWrapperDeserializer来进行反序列化。
 * 在RedisEntityWrapperDeserializer中会解析出RedisEntityWrapper的type属性和entity属性，然后将entity进行类型转换，包装返回一个真实的RedisEntityWrapper对象。
 * </p>
 *
 * @author linya 2016-02-22
 */
public class FastJsonRedisSerializer implements RedisSerializer<Object> {

    private static final Logger logger = Logger.getLogger(FastJsonRedisSerializer.class);

    //保存Class类型关系，避免多次调用Class.forName(clazz)
    private static final ConcurrentMap<String, Class<?>> entityClassMap = new ConcurrentHashMap<String, Class<?>>();

    //设置全局反序列化器，设置RedisEntityWrapper类型的反序列化器为RedisEntityWrapperDeserializer
    private static final ParserConfig parserConfig = ParserConfig.getGlobalInstance();

    static {
        parserConfig.putDeserializer(RedisEntityWrapper.class, new RedisEntityWrapperDeserializer());
    }

    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            //获取包装对象，并返回真实的缓存源对象
            RedisEntityWrapper wrapper = JSONObject.parseObject(bytes, RedisEntityWrapper.class);
            Object t = wrapper.getEntity();
            return t;
        } catch (Exception e) {
            logger.error(e);
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }

    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        //将缓存源数据包装成RedisEntityWrapper，并缓存json形式到Redis中
        RedisEntityWrapper wrapper = new RedisEntityWrapper();
        wrapper.setEntity(t);
        wrapper.setType(t.getClass());
        try {
            return JSONObject.toJSONBytes(wrapper);
        } catch (Exception e) {
            logger.error(e);
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    /**
     * 定义缓存源对象的包装类型，可以包装任何类型的缓存源
     */
    @SuppressWarnings({"serial"})
    private static class RedisEntityWrapper implements Serializable {

        Class<?> type;
        Object entity;

        RedisEntityWrapper() {
        }

        RedisEntityWrapper(Class<?> type, Object entity) {
            this.type = type;
            this.entity = entity;
        }

        @SuppressWarnings("unused")
        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public Object getEntity() {
            return entity;
        }

        public void setEntity(Object entity) {
            this.entity = entity;
        }

    }

    /**
     * RedisEntityWrapper对象的反序列化实现类，负责反序列化RedisEntityWrappe类型
     */
    private static class RedisEntityWrapperDeserializer implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            //解析出RedisEntityWrapper对象
            JSONObject jsonObject = parser.parseObject();
            //获取RedisEntityWrapper中的type，如果不存在，则返回空的RedisEntityWrapper
            String clazz = jsonObject.getString("type");
            if (clazz == null) {
                return (T) new RedisEntityWrapper();
            }
            //解析出Class，并保存起来，避免多次获取
            Class<?> entityClass = getEntityClass(clazz);
            //获取RedisEntityWrapper中的entity，将entity转化成真实的entityClass类型，然后重新包装到RedisEntityWrapper中
            Object entity = jsonObject.get("entity");
            entity = TypeUtils.cast(entity, entityClass, parserConfig);
            RedisEntityWrapper wrapper = new RedisEntityWrapper(entityClass, entity);
            return (T) wrapper;
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LBRACE;
        }
    }

    /**
     * 如果是clazz是List，则向上转型，解决JSONArray对象无法转成List具体子类问题
     *
     * @param clazz
     * @see TypeUtils#cast(Object, Class, ParserConfig)
     */
    private static Class<?> getEntityClass(String clazz) {
        Class<?> entityClass = entityClassMap.get(clazz);
        if (entityClass == null) {
            try {
                entityClass = Class.forName(clazz);
                if (List.class.isAssignableFrom(entityClass)) {
                    entityClass = List.class;
                }
                entityClassMap.put(clazz, entityClass);
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
        }
        return entityClass;
    }
}
