package seven.xiaoqiyiye.base.common;

import java.util.HashMap;
import java.util.Map;


public class ReturnResult {
	
	private static enum Result{
		SUCCESS("SUCCESS", "成功!"), FAILURE("FAILURE", "失败!");
		
		String code;
		String message;
		
		private Result(String code, String message){
			this.code = code;
			this.message = message;
		}
		
		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		static Result getResult(boolean success){
			return success ? SUCCESS : FAILURE;
		}
		
	}
	
	public static ReturnResult SUCCESS_RESULT = new ReturnResult();
	
	public static ReturnResult FAILURE_RESULT = new ReturnResult(false);

	boolean success;			//是否成功
	
	String resultCode;			//返回编号
	
	String resultMessage;		//返回信息
	
	Object attachment;			//附件对象
	
	Map<String, Object> extra;	//额外扩展数据
	
	public ReturnResult() {
		this(true);
	}
	
	public ReturnResult(boolean success){
		this(success, Result.getResult(success).getCode(), Result.getResult(success).getMessage());
	}
	
	public ReturnResult(String resultMessage){
		this(false, Result.getResult(false).getCode(), resultMessage);
	}
	
	public ReturnResult(boolean success, String resultMessage){
		this(success, Result.getResult(success).getCode(), resultMessage);
	}
	
	public ReturnResult(boolean success, String resultCode, String resultMessage){
		this.success = success;
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.extra = new HashMap<String, Object>();
	}
	
	public void addExtra(String key, Object val){
		extra.put(key, val);
	}
	
	public Object getExtra(String key){
		return extra.get(key);
	}
	
	public Map<String, Object> getExtra(){
		return extra;
	}
	
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>(extra);
		map.put("success", success);
		map.put("resultCode", resultCode);
		map.put("resultMessage", resultMessage);
		map.put("attachment", attachment);
		return map;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
	
}
