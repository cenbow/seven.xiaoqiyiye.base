package seven.xiaoqiyiye.base.common.highcharts;


public interface GroupObjectFactory<T extends StatisticsObject> extends NameConvertor{
	
	String BLANK_GROUP_KEY = " ";
	/**
	 * 获取一个StatisticsObject的所应该在的分组位置，使用GroupObject来保存
	 * @author linya Dec 27, 2014 2:19:21 PM
	 * @param t
	 * @return
	 */
	GroupObject group(T t);
}
