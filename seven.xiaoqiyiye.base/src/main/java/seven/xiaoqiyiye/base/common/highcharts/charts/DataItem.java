package seven.xiaoqiyiye.base.common.highcharts.charts;


/**
 * <p>
 * ChartsData中的数据项
 * </p>
 * @author linya Dec 29, 2014 8:49:21 AM
 * @version V1.0   
 */
public abstract class DataItem {
	
	protected String name;
	
	public String getName(){
		return this.name;
	}
	
	
    public void setName(String name) {
    	this.name = name;
    }


	public abstract Object getData();
	
}
