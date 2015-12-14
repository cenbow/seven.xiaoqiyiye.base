package com.cairenhui.core.base.invoke;

import javax.servlet.http.HttpServletRequest;

import com.cairenhui.core.base.invoke.handler.ServiceNoRequestHandler;
import com.cairenhui.core.base.invoke.param.AbstractRequestParam;
import com.cairenhui.core.base.invoke.response.QueryResponse;

/**
 * 请求中台的工具类，该类对RequestHandler、RequestParam、Response进行了封装
 * 该类可以灵活地选择RequestHandler和RequestParam进行请求，
 * 返回的响应对象为设置的responseClass类型
 * 
 * <code>
 * 		BaseRequestParam param = new BaseRequestParam();
 * 		param.setUserId(userId);
 * 		param.setEnumMiddleFuncNo(EnumMiddleFuncNo.OBS_MIDDLE_FUNC_QUERY_USER_ACCOUNTS);
 * 		QueryResponse<UserAccountResponse> response = RequestHandlerBuilder.build(FunctionNoRequestHandler.class)
 * 															.setRequest(req)
 * 															.setRequestParam(param)
 * 															.setResponseClass(UserAccountResponse.class)
 * 															.query();
 * 		List<UserAccountResponse> list = response.getReturnList();
 * </code>
 * 
 * 
 * <code>
 * 		relativeParam.setUserId(userId);
 * 		relativeParam.setEnumMiddleFuncNo(EnumMiddleFuncNo.GEM_SECOND_PERSON_EDIT);
 * 		SubmitResponse response = RequestHandlerBuilder.build(FunctionNoRequestHandler.class)
 * 															.setRequest(request)
 * 															.setRequestParam(relativeParam)
 * 															.setResponseClass(SubmitResponse.class)
 * 															.submit();
 * </code>
		
 * @author linya 2015-11-30
 *
 */
public class RequestHandlerBuilder {

	RequestHandler handler;
	
	RequestParam param;
	
	HttpServletRequest request;
	
	Class<? extends Response> responseClass;
	
	private RequestHandlerBuilder(){
		
	}
	
	public static RequestHandlerBuilder build(Class<? extends RequestHandler> handler){
		RequestHandlerBuilder builder = new RequestHandlerBuilder();
		try {
			builder.handler = handler.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	public RequestHandlerBuilder setRequest(HttpServletRequest request){
		this.request = request;
		return this;
	}
	
	public RequestHandlerBuilder setRequestParam(RequestParam param){
		this.param = param;
		if(request != null){
			((AbstractRequestParam)this.param).setRequest(request);
		}
		return this;
	}
	
	public RequestHandlerBuilder setResponseClass(Class<? extends Response> responseClass){
		this.responseClass = responseClass;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Response> T submit(){
		T response = null;
		try {
			response = (T) handler.submit(param, responseClass);
		} catch (RequestHandlerException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Response> QueryResponse<T> query(){
		QueryResponse<T> response = null;
		try {
			response = (QueryResponse<T>) handler.query(param, responseClass);
		} catch (RequestHandlerException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static <T extends Response> T submitServiceNo(RequestParam param, Class<? extends Response> responseClass){
		return build(ServiceNoRequestHandler.class).setRequestParam(param).setResponseClass(responseClass).submit();
	}
	
	public static <T extends Response> QueryResponse<T> queryServiceNo(RequestParam param, Class<? extends Response> responseClass){
		return build(ServiceNoRequestHandler.class).setRequestParam(param).setResponseClass(responseClass).query();
	}
}
