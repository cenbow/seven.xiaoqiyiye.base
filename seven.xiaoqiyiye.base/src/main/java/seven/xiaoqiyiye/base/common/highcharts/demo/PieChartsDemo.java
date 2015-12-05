package seven.xiaoqiyiye.base.common.highcharts.demo;

import java.util.ArrayList;
import java.util.List;

import seven.xiaoqiyiye.base.common.highcharts.ChartsHolder;
import seven.xiaoqiyiye.base.common.highcharts.HighChartsException;
import seven.xiaoqiyiye.base.common.highcharts.charts.ChartsData;
import seven.xiaoqiyiye.base.common.highcharts.support.PieChartsHolder;

import net.sf.json.JSONObject;



/**
 * <p>
 * 	这是使用HighCharts统计数据时，展现线性图的一个Demo。
 * 	线性图和柱状图数据格式是一样的。
 * 步骤：
 *      1.新建ChartsHolder对象
 *      2.设置统计数据
 *      3.设置x轴上的命名规则（可以省略）
 *      4.获取统计后的对象ChartsData
 * </p>
 * @author linya Jan 4, 2015 4:42:40 PM
 * @version V1.0   
 */
public class PieChartsDemo {
	
	private static List<PieStatisticsObject> statisticsData = new ArrayList<PieStatisticsObject>();
	
	private static void initStatisticsData(){
		//TimeStatisticsObject（类型，数量）
		PieStatisticsObject object1 = new PieStatisticsObject(TYPE.ONE.type,   100);
		PieStatisticsObject object2 = new PieStatisticsObject(TYPE.TWO.type,   750);
		PieStatisticsObject object3 = new PieStatisticsObject(TYPE.THREE.type, 600);
		statisticsData.add(object1);
		statisticsData.add(object2);
		statisticsData.add(object3);
	}
	
	
	
	public static void main(String[] args) throws HighChartsException {
	    
		initStatisticsData();
		
		/////  HighCharts 饼图的实现
		
		//1.处理饼图的对象
		ChartsHolder<PieStatisticsObject> lineHolder = new PieChartsHolder<PieStatisticsObject>();
		
		//2.设置好统计的数据信息
		lineHolder.setStatisticsObjectDatas(statisticsData);
		
		//3.设置x轴在页面上显示的名称（这个也可以不设置，不设置时将不会名称转换，使用key返回页面显示）
		lineHolder.setNameConvertor(new NameConvertor() {
			
			@Override
			public String convert(String key) {
				//这里的key是x轴项的值
				return TYPE.getNameByType(key);
			}
		});
		
		//4.获取统计好的图表数据
		ChartsData lineData = lineHolder.getChartData();
		
		//打印数据结构
		String jsonStr = JSONObject.fromObject(lineData).toString();
		System.out.println(jsonStr);
		
		//饼图数据没有categories
		/*
		 * {
		 * 		"categories":[],
		 * 		"seriesDatas":[
		 * 			{"data":100,"name":"类型一","selected":false,"sliced":false,"y":100},
		 * 			{"data":750,"name":"类型二","selected":false,"sliced":false,"y":750},
		 * 			{"data":600,"name":"类型三","selected":false,"sliced":false,"y":600}
		 * 		]
		 * }
		 * 
		 */
    }

}
