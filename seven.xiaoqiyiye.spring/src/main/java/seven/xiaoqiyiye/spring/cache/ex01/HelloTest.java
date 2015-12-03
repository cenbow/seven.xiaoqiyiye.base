package seven.xiaoqiyiye.spring.cache.ex01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="applicationContext.xml")
public class HelloTest extends AbstractJUnit4SpringContextTests{

	@Test
	public void hello(){
		HelloService service = applicationContext.getBean(HelloService.class);
		service.get("linya");
		service.put(new Hello("linya"));
		Hello hello = service.get("linya");
		System.out.println(hello.toString());
	}
}
