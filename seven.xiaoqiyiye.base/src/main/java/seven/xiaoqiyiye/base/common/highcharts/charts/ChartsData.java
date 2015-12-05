package seven.xiaoqiyiye.base.common.highcharts.charts;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 处理饼图、柱状图、线性图的数据对象
 * </p>
 * @author linya Dec 29, 2014 8:48:45 AM
 */
public class ChartsData {
	
	private List<String> categories;
	
	private List<DataItem> seriesDatas = new ArrayList<DataItem>();
	
	public ChartsData(){}
	
	public ChartsData(List<String> categories){
		this.categories = categories;
	}
	
	public ChartsData(List<String> categories, List<DataItem> seriesDatas){
		this.categories = categories;
		this.seriesDatas = seriesDatas;
	}
	
	
	public List<String> getCategories() {
		return categories;
	}
	
    public void setCategories(List<String> categories) {
    	this.categories = categories;
    }

    
	public ChartsData addSeriesItem(DataItem dataItem){
		seriesDatas.add(dataItem);
		return this;
	}
	
	public ChartsData setSeriesDatas(List<DataItem> dataItems){
		seriesDatas.addAll(dataItems);
		return this;
	}
	
    public List<DataItem> getSeriesDatas() {
    	return seriesDatas;
    }

}
