package com.cairenhui.core.base.invoke.response;

import com.cairenhui.core.base.invoke.Param;
import com.cairenhui.core.base.invoke.ParamType;

public class BaseSubmitResponse extends SubmitResponse {

	@Param(value="user_id", type=ParamType.LONG)
	Long userId;
	
	@Param(value="fund_account")
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
