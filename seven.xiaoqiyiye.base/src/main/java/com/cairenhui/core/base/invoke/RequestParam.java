package com.cairenhui.core.base.invoke;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义查询参数接口
 * 提供两种调用方式:ServiceNo和FuncNo
 * ServiceNo可以直接请求到中台Service层
 * FuncNo需要写中台Action层
 * @author linya 2015-11-27
 *
 */
public interface RequestParam {

	/**
	 * 将RequestParam对象的属性处理成Map
	 * 如果属性没有加@Param注解，则按照属性名作为key，
	 * 如果属性设置了@Param注解并且enabled为true，则按注解作为key。（enabled=false，表示该属性不加入Map参数）
	 */
	Map<String, Object> toMap();
	
	/**
	 * 请求的HttpServletRequest对象
	 */
	HttpServletRequest getRequest();
	
	/**
	 * 获取中台的服务号
	 */
	String getMiddleServiceNo();
	
}
