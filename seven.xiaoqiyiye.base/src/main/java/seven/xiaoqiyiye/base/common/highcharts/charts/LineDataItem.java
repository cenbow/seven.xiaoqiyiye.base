package seven.xiaoqiyiye.base.common.highcharts.charts;

import java.util.ArrayList;
import java.util.List;


public class LineDataItem extends DataItem {
	
	List data;
	
	public LineDataItem(){
		
	}
	
    public LineDataItem(String name) {
	    this.name = name;
	    this.data = new ArrayList();
    }
    
    public LineDataItem(String name, List data){
    	this.name = name;
    	if(data == null){
    		data = new ArrayList();
    	}
    	this.data = data;
    }
    
    public void addItem(Object itemData){
    	data.add(itemData);
    }

	
    public List getData() {
    	return data;
    }

	
    public void setData(List data) {
    	this.data = data;
    }
    
    
}
