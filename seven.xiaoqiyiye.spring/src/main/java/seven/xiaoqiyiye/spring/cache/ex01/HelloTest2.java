package seven.xiaoqiyiye.spring.cache.ex01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloTest2 {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		HelloService service = context.getBean(HelloService.class);
		service.put(new Hello("linya"));
		Hello hello3 = service.get("linya");
		System.out.println(hello3.toString());
		System.exit(0);
	}
}
