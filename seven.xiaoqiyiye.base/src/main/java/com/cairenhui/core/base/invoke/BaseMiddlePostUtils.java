package com.cairenhui.core.base.invoke;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * 请求中台的工具类
 * 给类功能从BaseMiddleClientService迁移过来
 * @author linya 2015-10-19
 * 
 */
public class BaseMiddlePostUtils {

	private static final int REQUEST_TYPE_SUBMIT = 0;

	private static final int REQUEST_TYPE_QUERY = 1;
	
	private static final String INVOKE = "invoke";

	private static Logger logger = Logger.getLogger(BaseMiddlePostUtils.class);
	
	
	private Map<String, Object> requestParam = new HashMap<String, Object>();
	
	private HttpServletRequest request;
	
	
	private BaseMiddlePostUtils(){
		
	}
	
	
	/**
	 * 构造BaseMiddlePostUtils对象
	 * @param request
	 * @return
	 */
	public static BaseMiddlePostUtils newBuider(HttpServletRequest request){
		return newBuilder(request, null);
	}
	
	public static BaseMiddlePostUtils newBuilder(HttpServletRequest request, Map<String, Object> requestMap){
		BaseMiddlePostUtils builder = new BaseMiddlePostUtils();
		if(requestMap != null){
			builder.requestParam.putAll(requestMap);
		}
		builder.request = request;
		return builder;
	}
	
	/**
	 * 添加请求参数
	 * @param key
	 * @param value
	 * @return
	 */
	public BaseMiddlePostUtils addParam(String key, Object value){
		requestParam.put(key, value);
		return this;
	}
	
	public BaseMiddlePostUtils addParam(Map<String, Object> requestMap){
		requestParam.putAll(requestMap);
		return this;
	}
	
	public BaseMiddlePostUtils setUserId(Long userId){
		addParam("user_id", userId);
		return this;
	}
	
	/**
	 * 通过功能号调用中台Action层方法(参考struts.xml配置)
	 * @author linya 2015-10-19
	 * @param func
	 * @param reqMap
	 * @param request
	 * @param requestType
	 * @return
	 */
	private Map<String, Object> request(String serviceNo, int requestType){
		
		Map<String, Object> res = null;
		try {
			//设置中台Action的功能号
			requestParam.put("function_no", serviceNo);
			res = null;//HttpPostHandle.httpPost(requestParam, requestType);
		} catch (Exception e) {
			logger.error(serviceNo + "出错：", e);
		}

		return res;
	}
	
	
	/**
	 * 调用中台（单条返回）
	 * @author linya 2015-10-19
	 * @param func
	 * @param reqMap
	 * @param request
	 * @return
	 */
	public Map<String, Object> submit(String middleServiceNo) {
		return invoke(middleServiceNo, REQUEST_TYPE_SUBMIT);
	}
	
	/**
	 * 调用中台（查询）
	 * @author linya 2015-10-19
	 * @param func
	 * @param reqMap
	 * @param request
	 * @return
	 */
	public Map<String, Object> query(String middleServiceNo) {
		return invoke(middleServiceNo, REQUEST_TYPE_QUERY);
	}	
	
	/**
	 * 通过服务号直接调用到中台Service层方法（建议使用这种方式）
	 * @author linya 2015-10-19
	 * @param middleServiceNo
	 * @param reqMap
	 * @param request
	 * @return
	 */
	private Map<String, Object> invoke(String middleServiceNo, int invokeType) {
		//设置中台方法的服务号
		requestParam.put("service_no", middleServiceNo);
		requestParam.put("invoke_type", invokeType);
		Map<String, Object> res = request(INVOKE, invokeType);
		return res;
	}
	
}
