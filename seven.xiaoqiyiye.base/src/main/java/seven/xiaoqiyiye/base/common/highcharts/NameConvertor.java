package seven.xiaoqiyiye.base.common.highcharts;



/**
 * <p>
 * 将key的名称进行转化，主要用于界面的显示
 * </p>
 * @author linya Dec 29, 2014 8:43:45 AM
 * @version V1.0   
 */
public interface NameConvertor {

	/**
	 * 转化显示在报表中的名称
	 * @author linya Dec 26, 2014 8:06:29 AM
	 * @param obj
	 * @return
	 */
	String convert(String key);
	
}
