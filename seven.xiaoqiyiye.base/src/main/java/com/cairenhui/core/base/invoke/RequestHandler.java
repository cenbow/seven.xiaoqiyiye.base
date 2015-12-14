package com.cairenhui.core.base.invoke;

import com.cairenhui.core.base.invoke.handler.ServiceNoRequestHandler;
import com.cairenhui.core.base.invoke.response.QueryResponse;

/**
 * 请求处理对象
 * RequestParam 封装请求参数，
 * 向中台发起http请求，返回指定的responseClass类型的响应实例。
 * @author linya 2015-11-30
 * @see FunctionNoRequestHandler
 * @see ServiceNoRequestHandler
 */
public interface RequestHandler {

	/**
	 * 获取单一的响应对象
	 * @param requestParam
	 * @param responseClass
	 * @return
	 * @throws RequestHandlerException
	 */
	<T extends Response> T submit(RequestParam requestParam, Class<T> responseClass) throws RequestHandlerException;
	
	/**
	 * 获取查询响应对象，会对应一个List数据，比如分页查询
	 * @param requestParam
	 * @param responseClass
	 * @return
	 * @throws RequestHandlerException
	 */
	<T extends Response> QueryResponse<T> query(RequestParam requestParam, Class<T> responseClass) throws RequestHandlerException;
	
}
