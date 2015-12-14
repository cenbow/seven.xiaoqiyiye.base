package com.cairenhui.core.base.invoke.handler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import seven.xiaoqiyiye.base.util.DataUtils;

import com.cairenhui.core.base.invoke.BaseMiddlePostUtils;
import com.cairenhui.core.base.invoke.Param;
import com.cairenhui.core.base.invoke.ParamType;
import com.cairenhui.core.base.invoke.RequestHandler;
import com.cairenhui.core.base.invoke.RequestHandlerException;
import com.cairenhui.core.base.invoke.RequestParam;
import com.cairenhui.core.base.invoke.Response;
import com.cairenhui.core.base.invoke.response.QueryResponse;


public abstract class AbstractRequestHandler implements RequestHandler {

	private static final Logger log = Logger.getLogger(AbstractRequestHandler.class);
	
	@SuppressWarnings("unchecked")
	public <T extends Response> T submit(RequestParam queryParam, Class<T> responseClass)
			throws RequestHandlerException {
		Response response = null;
		try {
			//向中台发起Http请求，返回响应数据
			Map<String, Object> paramMap = queryParam.toMap();
			check(queryParam);
			BaseMiddlePostUtils builder = BaseMiddlePostUtils.newBuider(queryParam.getRequest()).addParam(paramMap);
			Map<String, Object> responseMap = internalSubmit(queryParam, builder);
			//将响应数据封装成Response实例
			response = (Response) responseClass.newInstance();
			populateResponse(response, responseMap);
		} catch (Exception e) {
			throw new RequestHandlerException("++++ RequestHandler 解析submit响应发生异常", e);
		}
		return (T) response;
	}

	@SuppressWarnings("unchecked")
	public <T extends Response> QueryResponse<T> query(RequestParam queryParam, Class<T> responseClass)
			throws RequestHandlerException {
		//向中台发起Http请求，返回响应数据
		Map<String, Object> paramMap = queryParam.toMap();
		check(queryParam);
		BaseMiddlePostUtils builder = BaseMiddlePostUtils.newBuider(queryParam.getRequest()).addParam(paramMap);
		Map<String, Object> responseMap = internalQuery(queryParam, builder);
		//将响应数据封装成QueryResponse实例
		QueryResponse<T> queryResponse = new QueryResponse<T>();
		populateResponse(queryResponse, responseMap);
		
		List<T> returnList = new ArrayList<T>();
		List<Map<String, Object>> listData = (List<Map<String, Object>>) responseMap.get("resultList");
		if(listData != null){
			try {
				for(Map<String, Object> data: listData){
						Response response = (Response) responseClass.newInstance();
						populateResponse(response, data);
						returnList.add((T)response);
				}
			} catch (Exception e) {
				throw new RequestHandlerException("++++ RequestHandler 解析query响应发生异常", e);
			}
			queryResponse.setReturnList(returnList);
		}
		return queryResponse;
	}

	/**
	 * 检测请求参数的合法性
	 * @param queryParam
	 * @throws RequestHandlerException
	 */
	private void check(RequestParam queryParam) throws RequestHandlerException{
		HttpServletRequest request = queryParam.getRequest();
		if(request == null){
			throw new RequestHandlerException("++++ HttpServletRequest为空.");
		}
		
		if(!preInvoke(queryParam)){
			throw new RequestHandlerException("++++ RequestHandler 调用前置条件错误");
		}
	}
	
	/**
	 * 请求参数前置条件检测
	 * @param queryParam
	 * @return
	 * @throws RequestHandlerException
	 */
	protected boolean preInvoke(RequestParam queryParam) throws RequestHandlerException{
		return true;
	}
	
	abstract Map<String, Object> internalSubmit(RequestParam queryParam, BaseMiddlePostUtils builder);
	
	abstract Map<String, Object> internalQuery(RequestParam queryParam, BaseMiddlePostUtils builder);
	
	
	/**
	 * 构建响应Response对象
	 * @param response
	 * @param responseMap
	 */
	private void populateResponse(Response response, Map<String, Object> responseMap) {
		if(responseMap == null){
			return;
		}
		for(Class<?> clazz = response.getClass(); clazz != null; clazz = clazz.getSuperclass()){
			if(Response.class.isAssignableFrom(clazz)){
				populateResponse(response, clazz, responseMap);
			}
		}
	}
	
	private void populateResponse(Response response, Class<?> clazz, Map<String, Object> responseMap){
		Field currentFiled = null;
		try {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f: fields){
				currentFiled = f;
				Param paramAnnotation = f.getAnnotation(Param.class);
				String param = null;
				ParamType paramType = null;
				if(paramAnnotation != null){
					if(!paramAnnotation.enabled()){
						continue;
					}
					param = paramAnnotation.value();
					paramType = paramAnnotation.type();
				}else{
					param = f.getName();
					paramType = ParamType.STRING;
				}
				
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, f.getName());
				Method method = pd.getWriteMethod();
				if(method == null){
					continue;
				}
				
				//处理多值匹配的情况
				String[] keys = param.split(",");
				for(String key: keys){
					Object oldVal = responseMap.get(key.trim());
					Object vaule = convertType(paramType, oldVal);
					method.invoke(response, vaule);
					if(oldVal != null){
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("解析响应发生错误:" + clazz.getName() + ":" + currentFiled.getName(), e);
		}
	}
	
	/**
	 * Java类型处理
	 * @param paramType
	 * @param value
	 * @return
	 */
	private Object convertType(ParamType paramType, Object value){
		if(paramType == ParamType.STRING){
			return DataUtils.toString(value);
		}else if (paramType == ParamType.BOOLEAN) {
			return DataUtils.toBoolean(value);
		}else if (paramType == ParamType.INT) {
			return DataUtils.toInt(value);
		}else if (paramType == ParamType.LONG) {
			return DataUtils.toLong(value);
		}else if (paramType == ParamType.FLOAT) {
			return DataUtils.toFloat(value);
		}else if (paramType == ParamType.DOUBLE) {
			return DataUtils.toDouble(value);
		}else if (paramType == ParamType.DATE) {
			return DataUtils.toDate(DataUtils.toString(value), "yyyy-MM-dd HH:mm:ss");
		}else if (paramType == ParamType.CALENDER) {
			return DataUtils.toCalendar(DataUtils.toString(value), "yyyy-MM-dd HH:mm:ss");
		}
		return null;
	}
}
