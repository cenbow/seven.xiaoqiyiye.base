package seven.xiaoqiyiye.spring.aop.ex02;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Service service = (Service) context.getBean("proxyEx2Service");
		service.test();
	}
}
