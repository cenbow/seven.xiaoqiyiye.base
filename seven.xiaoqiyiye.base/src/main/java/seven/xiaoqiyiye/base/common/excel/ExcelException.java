package seven.xiaoqiyiye.base.common.excel;

public class ExcelException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private static final String PREFIX = "++++ Excel导出异常:";
    
    public ExcelException(String msg){
	super(PREFIX + msg);
    }
    
    public ExcelException(String msg, Throwable t){
	super(PREFIX + msg, t);
    }
}
