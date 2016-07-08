package seven.xiaoqiyiye.base.common;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import seven.xiaoqiyiye.base.common.redis.JedisCache;
import seven.xiaoqiyiye.base.common.redis.JedisCacheFactory;
import seven.xiaoqiyiye.base.common.redis.JedisCallable;


/**
 * 生成随机序列号
 *
 * @author linya
 * @version V1.0.0
 * @date 2016-6-21
 */
public class JedisNumber {

    private static final String NUMBER_PREFIX = "jedis_number_";

    private static final JedisCache<NumberValue> cache = JedisCacheFactory.newInstance(NumberValue.class);

    private static final Lock lock = new ReentrantLock();


    /**
     * 获取自增序列号(没有前缀，长度18位)
     *
     * @return
     */
    public static String getNum() {
        return getNum("", 0);
    }

    /**
     * 获取自增序列号(有前缀，长度18位)
     *
     * @param prefix
     * @return
     */
    public static String getNum(String prefix) {
        return getNum(prefix, 0);
    }

    /**
     * 获取自增序列号(没有前缀，长度自定义)
     *
     * @return
     */
    public static String getNum(int length) {
        return getNum(null, length);
    }

    /**
     * 获取自增序列号
     *
     * @param prefix
     * @return
     */
    public static String getNum(String prefix, int length) {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final SimpleDateFormat dateSdf = new SimpleDateFormat("yyMMddHHmm");

        // 总长度最小18位
        final int len = Math.max(length, 18);
        // 前缀
        final String pre = (prefix == null) ? "" : prefix;
        // 当前时间
        final Date now = new Date();
        // 当前时间序列号值(10位)
        final String time = dateSdf.format(now);

        // 每天的key值
        final String key = NUMBER_PREFIX + pre + sdf.format(now);

        // 当天自增序列号值
        NumberValue number = null;

        try {
            lock.lock();
            number = cache.get(key, 1, TimeUnit.DAYS, new JedisCallable<NumberValue>() {
                public NumberValue call() {
                    return new NumberValue(len - pre.length() - time.length());
                }
            });
            increment(key, number);
        } finally {
            lock.unlock();
        }

        if (number == null) {
            throw new RuntimeException("++++ get jedis number is exception!");
        }

        return prefix + time + number.format();
    }

    /**
     * 当天的序列号值增加增加
     *
     * @param key
     * @param number
     */
    private static void increment(String key, NumberValue number) {
        number.increament();
        cache.set(key, number);
    }

    /**
     * 格式化数字
     *
     * @param number
     * @return
     */
    private static String formatNumber(int number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    @SuppressWarnings({"serial", "unused"})
    private static class NumberValue implements Serializable {

        // 自增码
        int value;

        String format;

        NumberValue() {
            this(0);
        }

        NumberValue(int length) {
            value = 0;
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append("0");
            }
            format = sb.toString();
        }

        public void increament() {
            value++;
        }

        public String format() {
            return formatNumber(value, format);
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }

}
