package seven.xiaoqiyiye.base.common.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;


/**
 * 自定义JedisConnectionFactory，自定义Jedis配置文件的处理
 *
 * @author linya 2016-01-19
 */
public class ConfigurationableJedisConnectionFactory extends JedisConnectionFactory implements FactoryBean<JedisConnectionFactory> {

    //主机名称
    @Value("${redis.master.name}")
    private String masterName;

    //主机地址
    @Value("${redis.master.hostName}")
    private String masterHost;

    //主机端口
    @Value("${redis.master.port}")
    private int masterPort;

    //哨兵地址(IP:PORT)，例如: 10.1.17.115:26179,10.1.17.115:26279,10.1.17.115:26379
    @Value("${redis.sentinels}")
    private String sentinelAddrs;

    //是否使用池
    @Value("${redis.master.userPool}")
    private boolean usePool;

    //主机密码
    @Value("${redis.master.password}")
    private String masterPwd;

    //是否配置哨兵模式
    @Value("${redis.sentines.enabled}")
    private boolean isSentinelAware = false;

    //哨兵服务集合
    private Set<RedisNode> sentinels;

    //是否初始化过哨兵
    private boolean initedSentinels = false;

    private ConfigurationableJedisConnectionFactory() {

    }

    private ConfigurationableJedisConnectionFactory(RedisSentinelConfiguration sentinelConfig) {
        super(sentinelConfig);
    }

    /**
     * 解析哨兵服务地址
     *
     * @return
     */
    private void parseSentinels() {
        if (!StringUtils.hasText(sentinelAddrs)) {
            throw new IllegalArgumentException("++++ MyJedisConnectionFactory must to be set sentinels property");
        }
        Set<RedisNode> sentinelsNodeSet = new HashSet<RedisNode>();

        String[] sentinelsArrays = sentinelAddrs.split(",");
        for (String sentinel : sentinelsArrays) {
            String[] hortAndPort = sentinel.split(":");
            if (hortAndPort.length != 2) {
                throw new IllegalArgumentException("++++ MyJedisConnectionFactory set sentinels is error:" + sentinels);
            } 
            String host = hortAndPort[0];
            int port = Integer.valueOf(hortAndPort[1]);
            RedisNode sentinelNode = new RedisNode(host, port);
            sentinelsNodeSet.add(sentinelNode);
        }

        sentinels = sentinelsNodeSet;
    }

    /**
     * 生成JedisConnectionFactory实例
     *
     * @return
     * @throws Exception
     */
    public JedisConnectionFactory getObject() throws Exception {
        //首次初始化哨兵服务集合
        if (!initedSentinels) {
            parseSentinels();
            initedSentinels = true;
        }

        //创建JedisConnectionFactory实例
        ConfigurationableJedisConnectionFactory jedis = null;
        if (isSentinelAware) {
            RedisNode masterNode = new RedisNode(masterHost, masterPort);
            masterNode.setName(masterName);
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            redisSentinelConfiguration.setMaster(masterNode);
            redisSentinelConfiguration.setSentinels(sentinels);
            jedis = new ConfigurationableJedisConnectionFactory(redisSentinelConfiguration);
        } else {
            jedis = new ConfigurationableJedisConnectionFactory();
            jedis.setPoolConfig(new JedisPoolConfig());
        }
        jedis.setPassword(masterPwd);
        jedis.setHostName(masterHost);
        jedis.setPort(masterPort);
        jedis.setUsePool(usePool);
        jedis.afterPropertiesSet();
        return jedis;
    }

    public Class<?> getObjectType() {
        return ConfigurationableJedisConnectionFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }

}