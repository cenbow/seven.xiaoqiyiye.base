package seven.xiaoqiyiye.spring.aop.ex01;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;


public class Aspect {

	public void before(JoinPoint jp){
		System.out.println("Before Advice: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());  
	}
	
	public void after(JoinPoint jp){
		System.out.println("Afger Advice: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());  
	}
	
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		long time = System.currentTimeMillis();  
		System.out.println("Around Adivce begining time: " + time + " ms");  
        Object retVal = pjp.proceed();  
        System.out.println("Around Adivce begining time: " + System.currentTimeMillis() + " ms"); 
        time = System.currentTimeMillis() - time;
        return retVal; 
	}
	
	public void afterReturning(Object result){
		System.out.println("After Returning Advice:" + result);
	}
	
	public void throwing(JoinPoint jp, Throwable ex){
		System.out.println("After Throwing Advice: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + " throw exception");  
        System.out.println(ex.getMessage());  
	}
	
}
