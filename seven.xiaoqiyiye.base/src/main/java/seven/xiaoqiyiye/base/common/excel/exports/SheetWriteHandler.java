package seven.xiaoqiyiye.base.common.excel.exports;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import seven.xiaoqiyiye.base.common.excel.ExcelException;
import seven.xiaoqiyiye.base.common.excel.SheetHandler;


public class SheetWriteHandler extends SheetHandler{
	
    boolean showNo;	//是否显示编号
    
    //需要写到Excel中的数据
    List<?> data;
    
    SheetWriteHandler(List<String> columnNames, List<String> fieldNames, List<?> data) throws ExcelException{
	super(columnNames, fieldNames);
	this.data = data;
    }

    SheetWriteHandler(List<String> columnNames, List<String> fieldNames, List<?> data, boolean showNo) throws ExcelException{
	this(columnNames, fieldNames, data);
	this.showNo = showNo;
    }
	
    void handle() throws ExcelException{
        //1.设置头行数据
        writeHeadRow();
        //2.设置数据
        int rowNo = 1;
        for(Object rowBean : data){
            Row row = sheet.createRow(rowNo);
            writeRow(row, rowNo++, rowBean);
        }
    }
	
	
    void writeHeadRow(){
	Row row = sheet.createRow(0);
	int col = 0;
	if(showNo){
	    Cell cell = row.createCell(col++);
	    cell.setCellValue("编号");
	}
	for(String columnName: columnNames){
	    Cell cell = row.createCell(col++);
	    cell.setCellValue(columnName);
	}
    }
	
    private void writeRow(Row row, int rowNo, Object rowBean) throws ExcelException{
		
	//首先判断是否设置了propDescs
	if(!isInitPropDescs){
	    setPropDescs(rowBean);
	}
	
	//设置行数据
	int col = 0;
	if(showNo){
	    Cell cell = row.createCell(col++);
	    cell.setCellValue(rowNo);
	    CellStyle cellStyle =  cell.getCellStyle();
	    cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
	    cell.setCellStyle(cellStyle);
	}
	String filedName = null;
	for(PropertyDescriptor pd: propDescs){
	    filedName = pd.getName();
	    Method method = pd.getReadMethod();
	    try {
		Object obj = method.invoke(rowBean);
		Cell cell = row.createCell(col++);
		if(obj != null){
		    cell.setCellValue(obj.toString());
		}
            } catch (Exception ignore) {
        	String column = filed2ColumnMap.get(filedName);
                throw new ExcelException("第" + rowNo + "行[" + column + "]数据设置失败");
            }
	}
    }
	
}
