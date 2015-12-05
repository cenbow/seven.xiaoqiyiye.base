package seven.xiaoqiyiye.base.common.highcharts.demo;

import seven.xiaoqiyiye.base.common.highcharts.StatisticsObject;



public class PieStatisticsObject extends StatisticsObject{
	
	/**
     * 序列化ID
     */
    private static final long serialVersionUID = -8629770102424808352L;

	private String type;

	
    public PieStatisticsObject() {
	    super();
    }
    
    public PieStatisticsObject(String type, int count){
    	this.type = type;
    	super.count = count;
    }

	@Override
    public String getKey() {
		return this.type;
    }
	
    public String getType() {
    	return type;
    }


	
    public void setType(String type) {
    	this.type = type;
    }

	
	
}
