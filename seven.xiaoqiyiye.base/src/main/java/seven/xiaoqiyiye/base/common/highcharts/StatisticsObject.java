package seven.xiaoqiyiye.base.common.highcharts;

import java.io.Serializable;


/**
 * <p>
 * 统计的数据对象，提供抽象的key键由子类去实现
 * </p>
 * @author linya Dec 29, 2014 8:44:26 AM
 * @version V1.0   
 */
public abstract class StatisticsObject implements Serializable{
	
	/**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;
    
	protected Object count;		
	
	public abstract String getKey(); //统计键的名称

	public Object getCount() {		//统计值的数目
    	return count;
    }
}
