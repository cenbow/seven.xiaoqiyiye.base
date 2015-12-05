package seven.xiaoqiyiye.base.common.highcharts;

import java.util.List;

import seven.xiaoqiyiye.base.common.highcharts.charts.ChartsData;



/**
 * 定义一个ChartsHolder对象来处理统计的结果信息，将统计结果生成为饼图、线性图、柱状图
 * @author linya Dec 28, 2014 6:43:12 PM
 * @version V1.0   
 */
public interface ChartsHolder<T> {
	
	
	/**
	 * 获取统计图生成的统计数据对象
	 * @author linya Dec 29, 2014 8:40:20 AM
	 * @return
	 */
	ChartsData getChartData();
	
	/**
	 * 设置待处理的统计数据
	 * @author linya Dec 29, 2014 8:40:48 AM
	 * @param data
	 */
	void setStatisticsObjectDatas(List<T> data);
	
	/**
	 * 设置现在在界面上的名称转化，将x分组项名称重命名
	 * @author linya Dec 29, 2014 8:41:08 AM
	 * @param convertor
	 */
	void setNameConvertor(NameConvertor convertor);
}
