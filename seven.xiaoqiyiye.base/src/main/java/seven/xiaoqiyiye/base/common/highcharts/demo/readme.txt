说明： highcharts包适用于对统计数据的处理
demo:  demo文件在highcharts.demo包下

使用说明：（4,5只用于线性图）

1.StatisticsObject抽象类： 	统计的数据对象，提供抽象的key键由子类去实现。该类提供了抽象方法getKey()，这个方法作为统计分组的依据。
							在饼图中，getKey为分组项；
							在线性图/柱状图中，getKey为x轴的分组项，并且getKey获取的值一定要与categories中匹配。
							用实现的StatisticsObject子类，去封装统计数据，一条数据对应一个对象。形成统计数据 statisticsData，最后调用ChartsHolder#setStatisticsObjectDatas(List data)方法
							
  
2.ChartsHolder:  定义一个ChartsHolder对象来处理统计的结果信息，将统计结果生成为饼图、线性图、柱状图。
				 该接口有两个实现类分别处理饼图、线性图/柱状图，PieChartsHolder 和 LineChartsHolder。
				 
3.NameConvertor： 作为显示在页面上名称的转化， 将x/y轴的key值转为可现实在页面上的信息。

4.GroupObject： 用于线性图时，主要用用分组数据。

5.GroupObjectFactory: 用于线性图时，主要是负责生成GroupObject对象。

