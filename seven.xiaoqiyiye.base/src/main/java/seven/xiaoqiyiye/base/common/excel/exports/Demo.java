package seven.xiaoqiyiye.base.common.excel.exports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import seven.xiaoqiyiye.base.common.excel.ExcelException;

public class Demo {

    public static void main(String[] args) {
	String[] columnNames = new String[]{"名称","年龄"};
	String[] fieldNames = new String[]{"name","age"};
	List<User> data = new ArrayList<User>();
	data.add(new User("seven", 23));
	data.add(new User("xiaoqi", 24));
	OutputStream os = null;
	try {
	    os = new FileOutputStream("E:\\xxx.xlsx");
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	}
	try {
	    SheetBuilder.newBuilder()
	      		.setShowNo(true)	
	      		.setSheetName("XXX")	
	      		.setColumnNames(columnNames)	
	      		.setFieldNames(fieldNames)	
	      		.setData(data)			
	      		.build()
	      		.export(os);
	} catch (ExcelException e) {
	    e.printStackTrace();
	}
    }
    
    static class User{
	String name;
	int age;
	
	User(String name, int age){
	    this.name = name;
	    this.age = age;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public int getAge() {
	    return age;
	}

	public void setAge(int age) {
	    this.age = age;
	}
	
	
    }
}
