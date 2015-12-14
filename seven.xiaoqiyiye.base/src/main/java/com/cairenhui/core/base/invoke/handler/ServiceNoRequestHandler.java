package com.cairenhui.core.base.invoke.handler;

import java.util.Map;

import com.cairenhui.core.base.invoke.BaseMiddlePostUtils;
import com.cairenhui.core.base.invoke.RequestHandlerException;
import com.cairenhui.core.base.invoke.RequestParam;


/**
 * 对应于中台服务号的请求
 * 
 * @author linya 2015-11-30
 *
 */
public class ServiceNoRequestHandler extends AbstractRequestHandler{

	protected boolean preInvoke(RequestParam queryParam)
			throws RequestHandlerException {
		String serviceNo = queryParam.getMiddleServiceNo();
		return serviceNo != null;
	}

	Map<String, Object> internalSubmit(RequestParam queryParam, BaseMiddlePostUtils builder) {
		return builder.submit(queryParam.getMiddleServiceNo());
	}

	Map<String, Object> internalQuery(RequestParam queryParam, BaseMiddlePostUtils builder) {
		return builder.query(queryParam.getMiddleServiceNo());
	}

}
