package seven.xiaoqiyiye.base.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 工具类-spring bean
 * @author linya 2016-01-25
 */
public class SpringContextUtils implements BeanFactoryPostProcessor{
	
	private static ListableBeanFactory ctx;

	/**
	 * 根据bean名称获取
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return ctx.getBean(name);
	}
	
	public static ListableBeanFactory getApplicationContext(){
		return ctx;
	}
	
	public static <T> Map<String, T> getBeansOfType(Class<T> type){
		return ctx.getBeansOfType(type);
	}
	
	public static <T> T getBean(Class<T> requiredType){
		return ctx.getBean(requiredType);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ctx = beanFactory;
	}
	
}
