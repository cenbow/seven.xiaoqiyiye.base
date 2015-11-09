package seven.xiaoqiyiye.base.common.excel.parse;

public class RowWrapper<T> {

	T object;			//包装的行对象
	
	int rowNo;			//行数
	
	boolean success;	//是否成功
	
	String errorMsg;	//如果不成功，记录的错误消息
	
	public RowWrapper(int rowNo, T object){
		this.rowNo = rowNo;
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
