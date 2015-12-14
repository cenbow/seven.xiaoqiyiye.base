package com.cairenhui.core.base.invoke.response;

import seven.xiaoqiyiye.base.util.ReturnResult;

import com.cairenhui.core.base.invoke.Param;
import com.cairenhui.core.base.invoke.ParamType;
import com.cairenhui.core.base.invoke.Response;

/**
 * 提交查询的响应基类
 * @author linya 2015-11-30
 *
 */
public class AbstractResponse extends ReturnResult implements Response {
	
	@Param(value="error_no, errorNo", type=ParamType.INT)
	private int errorNo;
	
	@Param(value="error_info, errorInfo", type=ParamType.STRING)
	private String errorInfo;
	
	/**
	 * 包装成简单的ReturnResult，减少其他属性信息的暴露
	 * @return
	 */
	public ReturnResult toResult(){
		return new ReturnResult(error(), errorNo, errorInfo);
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
		super.setErrorNo(errorNo);
		super.setError(error());
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
		super.setErrorInfo(errorInfo);
	}
	
	public boolean success(){
		return errorNo == 0;
	}
	
	public boolean error(){
		return errorNo != 0;
	}
}
