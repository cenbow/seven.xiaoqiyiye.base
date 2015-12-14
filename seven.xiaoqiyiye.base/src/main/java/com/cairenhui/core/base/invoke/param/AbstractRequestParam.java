package com.cairenhui.core.base.invoke.param;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.cairenhui.core.base.invoke.Param;
import com.cairenhui.core.base.invoke.RequestParam;

/**
 * 提供请求查询参数的抽象类
 * @author linya 2015-11-25
 *
 */
public abstract class AbstractRequestParam implements RequestParam{

	@Param(enabled=false)
	private static final Logger log = Logger.getLogger(AbstractRequestParam.class);
	
	@Param(enabled=false)
	private HttpServletRequest request;
	
	@Param(enabled=false)
	private String middleServiceNo;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getMiddleServiceNo() {
		return middleServiceNo;
	}

	public void setMiddleServiceNo(String middleServiceNo) {
		this.middleServiceNo = middleServiceNo;
	}


	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//如果没有设置functionNo和serviceNo，尝试从注解中获取
		Param paramAnnotation = this.getClass().getAnnotation(Param.class);
		if(paramAnnotation != null){
			String serviceNo = getMiddleServiceNo();
			if(serviceNo == null){
				this.setMiddleServiceNo(paramAnnotation.serviceNo());
			}
		}
		
		for(Class<?> clazz = this.getClass(); clazz != null; clazz = clazz.getSuperclass()){
			if(AbstractRequestParam.class.equals(clazz)){
				break;
			}
			map.putAll(toMap(clazz));
		}
		return map;
	}
	
	private Map<String, Object> toMap(Class<?> clazz){
		Map<String, Object> map = new HashMap<String, Object>();
		Field currentFiled = null;
		try {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f: fields){
				currentFiled = f;
				Param paramAnnotation = f.getAnnotation(Param.class);
				String param = null;
				if(paramAnnotation != null){
					if(paramAnnotation.enabled()){
						param = paramAnnotation.value();
					}
				}else{
					param = f.getName();
				}
				if(param != null){
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, f.getName());
					Method method = pd.getReadMethod();
					Object value = method.invoke(this);
					map.put(param, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("创建请求参数发生错误:" + clazz.getName() + ":" + currentFiled.getName(), e);
		}
		
		return map;
	}
	
}
