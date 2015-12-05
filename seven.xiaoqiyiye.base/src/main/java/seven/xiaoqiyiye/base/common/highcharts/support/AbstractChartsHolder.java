package seven.xiaoqiyiye.base.common.highcharts.support;

import java.util.List;

import seven.xiaoqiyiye.base.common.highcharts.ChartsHolder;
import seven.xiaoqiyiye.base.common.highcharts.NameConvertor;
import seven.xiaoqiyiye.base.common.highcharts.StatisticsObject;
import seven.xiaoqiyiye.base.common.highcharts.charts.ChartsData;


public abstract class AbstractChartsHolder<T extends StatisticsObject> implements ChartsHolder<T> {
	
	List<T> statisticsObjectDatas;	//统计的对象数据
	
	protected final NameConvertor DEFAULT_NAME_CONVERTOR = new DefaultNameConvertor();
	
	NameConvertor convertor; 

	@Override
    public void setStatisticsObjectDatas(List<T> data) {
	    this.statisticsObjectDatas = data;
    }
	
	
	@Override
    public void setNameConvertor(NameConvertor convertor) {
		this.convertor = convertor; 
    }

	
	protected void checkNameConvert(){
		if(null == convertor){
			convertor = DEFAULT_NAME_CONVERTOR;
		}
	}


	class DefaultNameConvertor implements NameConvertor{
		@Override
        public String convert(String key) {
			return key;
        }
	}


	public abstract ChartsData getChartData();
}
