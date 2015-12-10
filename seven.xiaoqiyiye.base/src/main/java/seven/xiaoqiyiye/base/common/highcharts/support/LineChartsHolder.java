package seven.xiaoqiyiye.base.common.highcharts.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import seven.xiaoqiyiye.base.common.highcharts.GroupObject;
import seven.xiaoqiyiye.base.common.highcharts.GroupObjectFactory;
import seven.xiaoqiyiye.base.common.highcharts.HighChartsException;
import seven.xiaoqiyiye.base.common.highcharts.StatisticsObject;
import seven.xiaoqiyiye.base.common.highcharts.charts.ChartsData;
import seven.xiaoqiyiye.base.common.highcharts.charts.LineDataItem;


public class LineChartsHolder<T extends StatisticsObject> extends AbstractChartsHolder<T>{

	private static final Logger log = Logger.getLogger(LineChartsHolder.class);
	
	List<String> categories;	//x分组项
	
	GroupObjectFactory<T> groupHandler;	//处理数据的分组
	
	List<String> yGroup = new ArrayList<String>();	//y分组项
	
	Map<String,T> yGroupObjMap = new HashMap<String, T>();	//存放y分组项对应的StatisticsObject对象，便于命名处理
	
	Map<String, Integer> categories2Index;	//存放x分组项在List中对应的位置，便于存在数据到对应的位置
	
	Map<String, T> categoriesObjMap = new HashMap<String, T>();		//存放x分组项对应的StatisticsObject对象，便于命名处理
	
	Map<String, Object[]> chartDataMap = new HashMap<String, Object[]>();	//存放分组处理后的数据，这个是最终形成的统计数据，存放到ChartsData中去

	public LineChartsHolder(List<String> categories, GroupObjectFactory<T> groupHandler) throws HighChartsException{
		//必须设置x分组，否则抛出异常
		setCategories(categories);
		//需要数据分组处理对象，来定义分组规则
		this.groupHandler = groupHandler;
	}

	@Override
    public ChartsData getChartData() {
		
		//处理数据
		for(T t: statisticsObjectDatas){
			//转化成分组对象
			GroupObject groupObject = groupHandler.group(t);
			//获取分组后的xKey和yKey
			String xKey = groupObject.getKeyX();
			String yKey = groupObject.getKeyY();
			
			//空处理，否则放弃这条数据（错误数据）
			if(null == xKey || null == yKey){
				continue;
			}
			//查找xKey在x分组项中的位置，否则放弃这条数据（错误数据，也可能是GroupHandler分组规则有问题导致）
			Integer index = categories2Index.get(xKey);
			if(null == index){
				continue;
			}
			
			//保存数据到chartDataMap中对应位置
			if(chartDataMap.containsKey(yKey)){
				Object[] rowData = chartDataMap.get(yKey);
				rowData[index] = groupObject.getValue();
			}else{
				//初始化所有数据项为0
				int size = categories.size();
				Object[] rowData = new Object[size];
				initCategoriesValue(rowData, size);
				//设置统计数据
				rowData[index] = groupObject.getValue();
				chartDataMap.put(yKey, rowData);
				//记录y分组项
				yGroup.add(yKey);
				//记录y分组项对应的StatisticsObject对象，用于做命名转化处理
				yGroupObjMap.put(yKey, t);
			}
		}
		
		
		ChartsData charts = new ChartsData();
		//遍历y分组项，一个y分组项对应一个LineDataItem对象
		for(String y: yGroup){
			LineDataItem item = new LineDataItem();
			
			//y分组项命名转化处理
			String name = groupHandler.convert(y);
			
			//设置DataItem名称和数据
			item.setName(name);
			item.setData(Arrays.asList(chartDataMap.get(y)));
			
			//将每一个y分组项对象存放到ChartsData中
			charts.addSeriesItem(item);
		}
		
		//x分组项命名转化处理
		List<String> categoriesNames = categoriesNameConvert();
		charts.setCategories(categoriesNames);
		
		//返回最终的统计数据对象
	    return charts;
    }
	
	/**
	 * 初始化所有统计数据为0
	 * @author linya Dec 27, 2014 3:28:07 PM
	 * @param rowList
	 * @param size
	 */
	private void initCategoriesValue(Object[] rowList, int size){
		for(int i = 0; i < size; i ++){
		    rowList[i] = 0;
		}
	}
	
    /**
     * 设置x分组项
     * @author linya Dec 28, 2014 3:28:32 PM
     * @param categories
     * @throws HighChartsException
     */
    private void setCategories(List<String> categories) throws HighChartsException{
    	
    	if(CollectionUtils.isEmpty(categories)){
    		HighChartsException ex = new HighChartsException("linecharts's categories is blank.");
    		log.error(ex.getMessage());
    		throw ex;
    	}
    	this.categories = categories;
    	
    	//记录x分组项所在序号位置
    	categories2Index = new HashMap<String, Integer>();
    	int i = 0;
    	for(String categ: categories){
    		categories2Index.put(categ, i++);
    	}
    }
	
    private List<String> categoriesNameConvert(){
    	//如果convertor为空，不做转化处理
    	if(null == convertor){
    		return categories;
    	}
    	
    	List<String> categoriesNames = new ArrayList<String>(categories.size());
    	for(String categ: categories){
    		categoriesNames.add(convertor.convert(categ));
    	}
    	return categoriesNames;
    }
}
