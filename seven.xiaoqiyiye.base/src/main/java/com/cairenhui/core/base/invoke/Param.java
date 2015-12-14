package com.cairenhui.core.base.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cairenhui.core.base.invoke.param.BaseRequestParam;

/**
 * 定义RequestParam的请求参数
 * enabled设置请求时是否需要字段参数，value指定参数名称，type指定参数类型(解析响应Response时用到)
 * @author linya 2015-11-25
 * @see BaseRequestParam
 */
@Target(value={ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	String serviceNo() default "";				//在Class上注解
	
	String value() default "";
	
	boolean enabled() default true;
	
	ParamType type() default ParamType.STRING;
	
}