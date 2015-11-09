package seven.ebatong.response;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONObject;

import seven.ebatong.ParamName;
import seven.ebatong.YbaResponse;

public class AbstractYbaResponse implements YbaResponse{

	protected boolean isSuccess;

	protected String orgResponseStr;
	
	protected String result;
	
	protected String errorMessage;
	
	protected String service;
	
	protected String partner;
	
	protected String inputCharset; 
	
	protected String signType;
	
	protected String sign;
	
	
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getOrgResponseStr() {
		return orgResponseStr;
	}

	public void setOrgResponseStr(String orgResponseStr) {
		this.orgResponseStr = orgResponseStr;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
