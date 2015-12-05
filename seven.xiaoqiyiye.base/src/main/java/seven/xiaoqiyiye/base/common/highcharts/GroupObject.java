package seven.xiaoqiyiye.base.common.highcharts;


/**
 * <p>
 * 这是一个分组对象，用于LineChartsHolder中，keyX一般是StatisticsObject中的key键，keyY为y项分组键
 * </p>
 * @author linya Dec 29, 2014 8:42:03 AM
 * @version V1.0   
 */
public class GroupObject {
	
	String keyX;
	
	String keyY;
	
	Object value;

	public GroupObject(){
		
	}
	
	public GroupObject(String keyX, String keyY, Object value){
		this.keyX = keyX;
		this.keyY = keyY;
		this.value = value;
	}
	
    public String getKeyX() {
    	return keyX;
    }

	
    public void setKeyX(String keyX) {
    	this.keyX = keyX;
    }

	
    public String getKeyY() {
    	return keyY;
    }

	
    public void setKeyY(String keyY) {
    	this.keyY = keyY;
    }

	
    public Object getValue() {
    	return value;
    }

	
    public void setValue(Object value) {
    	this.value = value;
    }

	
	
}
