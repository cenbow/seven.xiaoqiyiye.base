package seven.xiaoqiyiye.base.common.excel;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import seven.xiaoqiyiye.base.common.excel.ExcelException;

public class SheetHandler {
    
    protected Sheet sheet;
    
    protected List<String> columnNames;
    
    protected List<String> fieldNames;
    
    protected List<PropertyDescriptor> propDescs = new ArrayList<PropertyDescriptor>();
    
    protected Map<String, String> filed2ColumnMap;
    
    protected boolean isInitPropDescs;	//是否已经初始化了PropertyDescriptor
    
    protected SheetHandler(List<String> columnNames, List<String> fieldNames){
    	this.columnNames = columnNames;
    	this.fieldNames = fieldNames;
    	initField2CloumnMap();
    }
	
    protected void setPropDescs(Object rowBean){
	Class<?> clazz = rowBean.getClass();
        for(String fieldName: fieldNames){
            try {
        	PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
        	propDescs.add(pd);
            } catch (IntrospectionException ingore) {
                //ingore
            }
        }
        isInitPropDescs = true; 
    }
    
    protected void initField2CloumnMap(){
	
    	if(columnNames == null || columnNames.size() == 0
    		|| fieldNames == null  || fieldNames.size() == 0){
    	    throw new ExcelException("列名集合和属性集合名不能为空");
    	}
    	
    	if(columnNames.size() != fieldNames.size()){
    	    throw new ExcelException("列名集合和属性集合长度不相等");
    	}
    	
    	filed2ColumnMap = new HashMap<String, String>(columnNames.size());
    	for(int i = 0; i < columnNames.size(); i ++){
    	    filed2ColumnMap.put(fieldNames.get(i), columnNames.get(i));
    	}
	
    }
	
    public void setSheet(Sheet sheet) {
    	this.sheet = sheet;
    }
	
}
