package seven.xiaoqiyiye.spring.aop.ex02;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;


public class AfterAdvice implements AfterReturningAdvice {

	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		System.out.println("after returning...");
	}

}
