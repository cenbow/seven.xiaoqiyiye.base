package seven.xiaoqiyiye.base.common.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/common/redis.xml"})
public class JedisCacheTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void test(){
		JedisCache<String> jedis = JedisCacheFactory.newInstance(String.class);
		jedis.set("linya", "hello");
		System.out.println(jedis.get("linya"));
	}
}
