package com.cairenhui.core.base.invoke.response;

import java.util.ArrayList;
import java.util.List;

import com.cairenhui.core.base.invoke.Param;




public class QueryResponse<T> extends AbstractResponse{

	@Param(enabled=false)
	private static final QueryResponse<Object> FAILURE = new QueryResponse<Object>(-1);
	
	@Param(enabled=false)
	private List<T> returnList;
	
	public QueryResponse(){
		this(0);
	}
	
	private QueryResponse(int errorNo){
		this(errorNo, null);
	}
	
	public QueryResponse(int errorNo, String errorInfo){
		setErrorNo(errorNo);
		setErrorInfo(errorInfo);
	}

	public List<T> getReturnList() {
		return (returnList == null ? new ArrayList<T>() : returnList);
	}

	public void setReturnList(List<T> returnList) {
		this.returnList = returnList;
	}
	 
}
