package seven.xiaoqiyiye.base.common.highcharts.support;


import org.springframework.util.CollectionUtils;

import seven.xiaoqiyiye.base.common.highcharts.StatisticsObject;
import seven.xiaoqiyiye.base.common.highcharts.charts.ChartsData;
import seven.xiaoqiyiye.base.common.highcharts.charts.DataItem;
import seven.xiaoqiyiye.base.common.highcharts.charts.PieDataItem;



public class PieChartsHolder<T extends StatisticsObject> extends AbstractChartsHolder<T>{

	@Override
    public ChartsData getChartData() {
		ChartsData charts = new ChartsData();
		if(!CollectionUtils.isEmpty(statisticsObjectDatas)){
			for(StatisticsObject statis: statisticsObjectDatas){
				DataItem item = populteDataItem(statis);
				charts.addSeriesItem(item);
			}
		}
	    return charts;
    }
	
	
    /**
     * 将StatisticsObject组装成DataItem对象
     * @author linya Dec 29, 2014 8:51:10 AM
     * @param obj
     * @return
     */
    private DataItem populteDataItem(StatisticsObject obj) {
    	//组装对象
    	DataItem item = populteItem(obj);
		checkNameConvert();
		//命名处理
		String name = convertor.convert(obj.getKey());
		item.setName(name);
		return item;
    }
	
    
    /**
     * 该方法提供子类重写
     * @author linya Jan 4, 2015 5:12:10 PM
     * @param obj
     * @return
     */
    protected DataItem populteItem(StatisticsObject obj){
    	return new PieDataItem(obj.getKey(), obj.getCount());
    }
}
