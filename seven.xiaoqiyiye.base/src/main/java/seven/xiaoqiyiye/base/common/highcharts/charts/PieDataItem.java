package seven.xiaoqiyiye.base.common.highcharts.charts;


public class PieDataItem extends DataItem {
	
	private Object y;
	
	private boolean sliced;
	
	private boolean selected;
	
    
	public PieDataItem(String name, Object data) {
	    this(data, false, false);
    }
	
	public PieDataItem(Object data, boolean sliced, boolean selected) {
	    this.y = data;
	    this.sliced = sliced;
	    this.selected = selected;
    }
	

	@Override
	public Object getData() {
		return this.y;
	}

    public Object getY() {
    	return y;
    }

	
    public boolean isSliced() {
    	return sliced;
    }

	
    public void setSliced(boolean sliced) {
    	this.sliced = sliced;
    }

	
    public boolean isSelected() {
    	return selected;
    }

	
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
	
    
}
