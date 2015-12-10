package seven.xiaoqiyiye.base.common.highcharts;


public class HighChartsException extends Exception{

	private static String PREFIX = "++++ HighCharts Exception:"; 
	
	/**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;
    
    public HighChartsException(String msg){
    	super(PREFIX + msg);
    }
    
    public HighChartsException(Throwable e){
    	super(PREFIX, e);
    }
}
