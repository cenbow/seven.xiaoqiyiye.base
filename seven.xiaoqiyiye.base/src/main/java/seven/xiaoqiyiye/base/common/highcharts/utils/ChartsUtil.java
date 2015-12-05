package seven.xiaoqiyiye.base.common.highcharts.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import seven.xiaoqiyiye.base.util.DataUtils;


public class ChartsUtil {
	
	private static Logger log = Logger.getLogger(ChartsUtil.class);
	
	/**
	 * 设置按日统计时的Categories 例如：day = "2014-12-27"，
	 * 则返回["2014-12-27 01:00", "2014-12-27 02:00" ... "2014-12-27 23:00"]
	 * @author linya Dec 27, 2014 6:19:50 PM
	 * @param day  yyyy-MM-dd
	 * @return
	 */
	public static List<String> getHourCategories(String day){
    	List<String> hourly = new ArrayList<String>();
    	for(int i = 0; i < 24; i ++){
    		hourly.add(day + " " + format(i) + ":00");
    	}
    	log.info("++++ categories hourly:" + Arrays.toString(hourly.toArray()));
    	return hourly;
	}
	
	/**
	 * 设置按日统计时的Categories 例如：day = "2014-12-27"，
	 * 则返回["2014-12-27 01:00", "2014-12-27 02:00" ... "2014-12-27 23:00"]
	 * @author linya Dec 27, 2014 6:20:50 PM
	 * @param day
	 * @return
	 */
	public static List<String> getHourCategories(Calendar day){
		String d = DataUtils.format(day, "yyyy-MM-dd");
		return getHourCategories(d);
	}
	
	/**
	 * 设置按月统计时的Categories 例如：day = "2014-12"，
	 * 则返回["2014-12-01", "2014-12-02" ... "2014-12-31"]
	 * @author linya Dec 27, 2014 6:20:06 PM
	 * @param month yyyy-MM
	 * @return
	 */
	public static List<String> getDairyCategories(String month){
    	List<String> dairy = new ArrayList<String>();
    	Calendar currentMonth = DataUtils.toCalendar(month, "yyyy-MM");
    	int max = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
    	
    	for(int i = 1; i <= max; i ++){
    		dairy.add(month + "-" + format(i));
    	}
    	
    	log.info("++++ categories dairy:" + Arrays.toString(dairy.toArray()));
    	return dairy;
	}
	
	/**
	 * 设置按月统计时的Categories 例如：day = "2014-12-27"，
	 * 则返回["2014-12-01", "2014-12-02" ... "2014-12-31"]
	 * @author linya Dec 27, 2014 6:20:06 PM
	 * @param month yyyy-MM
	 * @return
	 */
	public static List<String> getDairyCategories(Calendar month){
		String m = DataUtils.format(month, "yyyy-MM");
		return getDairyCategories(m);
	}
	
	
	/**
	 * 设置按月统计时的Categories 例如：day = "2014"，
	 * 则返回["2014-12-01", "2014-12-02" ... "2014-12-31"]
	 * @author linya Dec 27, 2014 6:30:21 PM
	 * @param year yyyy
	 * @return
	 */
	public static List<String> getMonthlyCategories(String year){
    	List<String> monthly = new ArrayList<String>();
    	for(int i = 1; i <= 12; i ++){
    		monthly.add(year + "-" + format(i));
    	}
    	
    	log.info("++++ categories monthly:" + Arrays.toString(monthly.toArray()));
    	return monthly;
	}
	
	public static List<String> getMonthlyCategories(Calendar year){
    	String y = DataUtils.format(year, "yyyy");
    	return getMonthlyCategories(y);
	}
	
	/**
	 * 设置按年统计时的Categories 例如：day = "2014"，
	 * 则返回["2014-01", "2014-02" ... "2014-12"]
	 * @author linya Dec 28, 2014 4:48:23 PM
	 * @param time
	 * @param format yyyy |  yyyy-MM |  yyyy-MM-dd
	 * @return
	 */
	public static List<String> getTypeCategories(String format){
		if(null == format){
			return getHourCategories(Calendar.getInstance());
		}
		
		int splitLen = format.split("-").length;
		if(splitLen == 1){
			return getMonthlyCategories(format);
		}
		else if(splitLen == 2){
			return getDairyCategories(format);
		}
		else{
			return getHourCategories(format);	
		}
	}
	
	
	public static String format(Integer oldVal){
		if(null == oldVal){
			return "";
		}
		String newVal = MessageFormat.format("{0,number,00}", oldVal);
		return newVal;
	}
	
	public static String formatCategories(String year, String month, String day, String hour){
		StringBuffer buf = new StringBuffer(30);
    	String[] temp = {year,month,day,hour};
    	for(int i = 0; i < temp.length; i++){
    		if(StringUtils.isEmpty(temp[i])){
    			break;
    		}
    		if(i == 3){
    			buf.append(' ');
    		}else{
    			buf.append('-');
    		}
    		buf.append(temp[i]);
    		if(i == 3){
    			buf.append(":00");
    		}
    	}
    	buf.deleteCharAt(0);
    	return buf.toString();
	}
	
	public static String formatCategories(Integer year, Integer month, Integer day, Integer hour){
		return formatCategories(String.valueOf(year), format(month), format(day), format(hour));
	}
	
	/**
	 * yyyy-MM-dd HH:00 --->  HH
	 * yyyy-MM-dd       --->  dd
	 * yyyy-MM          --->  MM
	 * @author linya Dec 28, 2014 4:07:09 PM
	 * @param categories
	 * @return
	 */
	public static String parseTimeFormatCategories(String categories){
		if(StringUtils.isEmpty(categories)){
			return "";
		}
		int index = categories.indexOf(" ");
		if(index > 0){
			int end = categories.indexOf(":");
			return categories.substring(index, end);
		}
		index = categories.lastIndexOf("-");
		if(index > 0){
			return categories.substring(index+1);
		}
		return categories;
	}
	
	
	public static List<String> getDairyCategories(Calendar beginTime, Calendar endTime){
		//存放日期，如：[01,02,03,04]//这里考虑到页面上显示2014-08-01太长，所以只显示日期
    	List<String> dairy = new ArrayList<String>();
    	Calendar temp = (Calendar)beginTime.clone();
    	while(endTime.after(temp)){
    		String t = DataUtils.format(temp, "yyyy-MM-dd");
    		dairy.add(t);
    		temp.add(Calendar.DAY_OF_MONTH, 1);
    	}
    	log.info("++++ dairy categories monthly:" + Arrays.toString(dairy.toArray()));
    	return dairy;
	}
	
	
	public static List<String> getMonthlyCategories(Calendar beginTime, Calendar endTime){
		//定义存放月的数据，如：[2014-07，2014-08，2014-09]
    	List<String> monthly = new ArrayList<String>();
    	Calendar temp = (Calendar)beginTime.clone();
    	while(endTime.after(temp)){
    		String t = DataUtils.format(temp, "yyyy-MM");
    		monthly.add(t);
    		temp.add(Calendar.MONTH, 1);
    	}
    	log.info("++++ monthly categories:" + Arrays.toString(monthly.toArray()));
    	return monthly;
	}
}
