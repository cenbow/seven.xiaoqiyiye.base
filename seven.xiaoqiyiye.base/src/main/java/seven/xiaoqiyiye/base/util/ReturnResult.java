package seven.xiaoqiyiye.base.util;

import java.util.HashMap;
import java.util.Map;


public class ReturnResult {

	boolean error;	//错误标志
	
	int errorNo;		//错误号
	
	String errorInfo;	//错误信息
	
	Object data;		//正确是返回的数据
	
	Map<String, Object> extra;	//便于扩展数据
	
	
	public static ReturnResult SUCCESS = new ReturnResult();
	public static ReturnResult FAILURE = new ReturnResult(true);
	
	public ReturnResult() {
		this(false, 0, null);
	}
	
	public ReturnResult(Object data){
		this(false, 0, null);
		this.data = data;
	}
	
	public ReturnResult(boolean isError){
		this(isError, -1, null);
	}
	
	public ReturnResult(String errorInfo){
		this(true, -1, errorInfo);
	}
	
	public ReturnResult(boolean isError, int errorNo, String errorInfo){
		this.error = isError;
		this.errorNo = errorNo;
		this.errorInfo = errorInfo;
		extra = new HashMap<String, Object>();
	}
	
	public ReturnResult(Map<String, Object> resultMap){
		if(resultMap == null){
			this.errorNo = -1;
			this.error = true;
			this.errorInfo = "return null";
		}
		
		Object errorNo = resultMap.get("error_no") != null ? resultMap.get("error_no")
														: resultMap.get("errorNo") != null ? resultMap.get("errorNo") 
														: -1;
		this.errorNo = DataUtils.toInt(errorNo);
		
		Object errorInfo = resultMap.get("error_info") != null ? resultMap.get("error_info")
														: resultMap.get("errorInfo") != null ? resultMap.get("errorInfo") 
														: -1;
		this.errorInfo = DataUtils.toString(errorInfo);
		this.error = (this.errorNo != 0);
		this.extra = new HashMap<String, Object>();
	}
	
	public void addExtra(String key, Object val){
		extra.put(key, val);
	}
	
	public Object getExtra(String key){
		return extra.get(key);
	}
	
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>(extra);
		map.put("error", error);
		map.put("errorNo", errorNo);
		map.put("errorInfo", errorInfo);
		map.put("data", data);
		return map;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean isError) {
		this.error = isError;
	}
	
	public boolean isSuccess() {
		return !error;
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		if(extra != null){
			this.extra.putAll(extra);
		}
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
