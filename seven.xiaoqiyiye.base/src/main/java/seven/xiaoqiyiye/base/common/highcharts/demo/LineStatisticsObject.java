package seven.xiaoqiyiye.base.common.highcharts.demo;

import seven.xiaoqiyiye.base.common.highcharts.StatisticsObject;
import seven.xiaoqiyiye.base.common.highcharts.utils.ChartsUtil;



public class LineStatisticsObject extends StatisticsObject{
	
	/**
     * 序列化ID
     */
    private static final long serialVersionUID = -8629770102424808352L;

	private Integer year;
	
	private Integer month;
	
	private Integer day;
	
	private Integer hour;
	
	private String type;

	
    public LineStatisticsObject() {
	    super();
    }
    
    public LineStatisticsObject(String type, int count){
    	this.type = type;
    	super.count = count;
    }
    
    public LineStatisticsObject(int year, int month, int day, String type, int count){
    	this.year = year;
    	this.month = month;
    	this.day = day;
    	this.type = type;
    	super.count = count;
    }

	@Override
    public String getKey() {
		return ChartsUtil.formatCategories(year, month, day, hour);
    }

	public Integer getYear() {
    	return year;
    }

	
    public void setYear(Integer year) {
    	this.year = year;
    }

	
    public Integer getMonth() {
    	return month;
    }

	
    public void setMonth(Integer month) {
    	this.month = month;
    }

	
    public Integer getDay() {
    	return day;
    }

	
    public void setDay(Integer day) {
    	this.day = day;
    }

	
    public Integer getHour() {
    	return hour;
    }

	
    public void setHour(Integer hour) {
    	this.hour = hour;
    }


	
    public String getType() {
    	return type;
    }


	
    public void setType(String type) {
    	this.type = type;
    }

	
	
}
