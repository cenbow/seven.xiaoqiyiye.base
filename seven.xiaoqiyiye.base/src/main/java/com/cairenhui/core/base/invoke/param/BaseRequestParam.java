package com.cairenhui.core.base.invoke.param;

import com.cairenhui.core.base.invoke.Param;


/**
 * 请求参数的基础类
 * @author linya 2015-11-25
 *
 */
public class BaseRequestParam extends AbstractRequestParam{

	@Param("user_id")
	Long userId;
	
	@Param("fund_account")
	String fundAccount;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	
}