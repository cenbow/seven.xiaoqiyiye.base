package seven.xiaoqiyiye.spring.cache.ex01;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Hello {

	String name;
	
	String now;

	static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	
	public Hello(String name){
		this.name = name;
		Calendar cal = Calendar.getInstance();
		now = sdf.format(cal.getTime());
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Hello," + name + "," + now;
	}
	
	
}
