package seven.xiaoqiyiye.base.common.highcharts.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seven.xiaoqiyiye.base.common.highcharts.ChartsHolder;
import seven.xiaoqiyiye.base.common.highcharts.GroupObject;
import seven.xiaoqiyiye.base.common.highcharts.GroupObjectFactory;
import seven.xiaoqiyiye.base.common.highcharts.HighChartsException;
import seven.xiaoqiyiye.base.common.highcharts.NameConvertor;
import seven.xiaoqiyiye.base.common.highcharts.StatisticsObject;
import seven.xiaoqiyiye.base.common.highcharts.charts.ChartsData;
import seven.xiaoqiyiye.base.common.highcharts.support.LineChartsHolder;

import com.alibaba.fastjson.JSONObject;



/**
 * <p>
 * 	这是使用HighCharts统计数据时，展现线性图的一个Demo。
 * 	线性图和柱状图数据格式是一样的。
 * 步骤：
 *  	1.设置categories，categories作为数据x轴上的key
 *      2.设置GroupHandler， GroupHandler将数据y轴项分组，这个很重要，否则会导致数据统计错误
 *      3.新建ChartsHolder对象
 *      4.设置统计数据
 *      5.设置x轴上的命名规则（可以省略，省略时使用categories显示）
 *      6.获取统计后的对象ChartsData
 * </p>
 * @author linya Jan 4, 2015 4:42:40 PM
 * @version V1.0   
 */
public class LineChartsDemo {
	
	private static List<StatisticsObject> statisticsData = new ArrayList<StatisticsObject>();
	
	
	private static void initStatisticsData(){
		//TimeStatisticsObject（年，月，日，类型，数量）
		StatisticsObject object11 = new LineStatisticsObject(2014, 12, 10, TYPE.ONE.type,   100);
		StatisticsObject object12 = new LineStatisticsObject(2014, 12, 10, TYPE.TWO.type,   150);
		StatisticsObject object21 = new LineStatisticsObject(2014, 12, 20, TYPE.TWO.type,   400);
		StatisticsObject object22 = new LineStatisticsObject(2014, 12, 20, TYPE.THREE.type, 200);
		StatisticsObject object31 = new LineStatisticsObject(2014, 12, 30, TYPE.TWO.type,   200);
		StatisticsObject object32 = new LineStatisticsObject(2014, 12, 30, TYPE.THREE.type, 400);
		
		statisticsData.add(object11);
		statisticsData.add(object12);
		statisticsData.add(object21);
		statisticsData.add(object22);
		statisticsData.add(object31);
		statisticsData.add(object32);
	}
	
	
	
	public static void main(String[] args) throws HighChartsException {
	    
		initStatisticsData();
		
		/////  HighCharts 线性图的实现
		
		//1.categories为x轴分组项
		List<String> categories = new ArrayList<String>(Arrays.asList(new String[]{"2014-12-10","2014-12-20","2014-12-30"}));
		
		//2.GroupHandler用户处理y轴分组项
		GroupObjectFactory<StatisticsObject> groupHandler = new GroupObjectFactory<StatisticsObject>() {

            public GroupObject group(StatisticsObject t) {
				LineStatisticsObject tt = (LineStatisticsObject) t;
				//设置分组规则:
				//t.getKey()是x轴，这里是按照时间分类，
				//tt.getType()是y轴，这里是按照类型分类，
				//t.getCount()是分组数据
	            return new GroupObject(t.getKey(), tt.getType(), t.getCount());
            }

            public String convert(String key) {
				//设置y轴在页面上显示的名称，这里的key是y轴项的值
	            return TYPE.getNameByType(key);
            }
			
		};
		
		//3.处理线性图的对象
		ChartsHolder<StatisticsObject> lineHolder = new LineChartsHolder<StatisticsObject>(categories, groupHandler);
		
		//4.设置好统计的数据信息
		lineHolder.setStatisticsObjectDatas(statisticsData);
		
		//5.设置x轴在页面上显示的名称（这个也可以不设置，不设置时将不会名称转换，使用key返回页面显示）
		lineHolder.setNameConvertor(new NameConvertor() {
			
			public String convert(String key) {
				//这里的key是x轴项的值
				return key + ":00";
			}
		});
		
		//6.获取统计好的图表数据
		ChartsData lineData = lineHolder.getChartData();
		
		//打印数据结构
		String jsonStr = JSONObject.toJSONString(lineData);
		System.out.println(jsonStr);
		/*
		 * {
		 * 		"categories":["2014-12-10","2014-12-20","2014-12-30"],
		 * 		"seriesDatas":[
		 * 			{
		 * 				"data":[100,0,0],
		 * 				"name":"类型一"
		 * 			},
		 * 			{
		 * 				"data":[150,400,200],
		 * 				"name":"类型二"
		 * 			},
		 * 			{
		 * 				"data":[0,200,400],
		 * 				"name":"类型三"
		 * 			}
		 * 		]
		 * }
		 */
    }

}
