package seven.xiaoqiyiye.base.common.excel.parse;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import seven.xiaoqiyiye.base.common.excel.ExcelException;
import seven.xiaoqiyiye.base.common.excel.SheetHandler;

public class SheetReadHandler<T> extends SheetHandler {

	ExcelParseProcessor<T> parseProcessor;
	
	public SheetReadHandler(List<String> columnNames, List<String> fieldNames, ExcelParseProcessor<T> parseProcesor){
		super(columnNames, fieldNames);
		this.parseProcessor = parseProcesor;
	}

    void handle() throws ExcelException{
        //1.校验模板列
        boolean flag = validHeadRow();
        if(!flag){
            throw new ExcelException("Excel模板错误");
        }
        
        //2.读取数据
        for(int i = 1; i < sheet.getLastRowNum(); i ++){
        	T row = parseProcessor.getRowProcessor().createRow();
            writeRow(sheet.getRow(i), i, row);
        }
        
    }
	
	
    boolean validHeadRow(){
    	Row row = sheet.getRow(0);
    	int colNum = row.getLastCellNum() - row.getFirstCellNum();
    	if(colNum != columnNames.size()){
    	    return false;
    	}
    	
    	boolean flag = true;
    	if(row != null){
    	    for(int col = 0; col < colNum; col ++){
        		Cell cell = row.getCell(col);
        		String columnName = cell.getStringCellValue();
        		flag = columnNames.get(col).equals(columnName);
        		if(!flag){
        		    return flag;
        		}
    	    }
    	}
    	return flag;
    }
	
    private void writeRow(Row row, int rowNo, T rowBean) throws ExcelException{
		
    	//首先判断是否设置了propDescs
    	if(!isInitPropDescs){
    	    setPropDescs(rowBean);
    	}
    	
    	ExcelParseStatus<T> parseStatus = parseProcessor.getProcessStatus();
    	parseStatus.getParseRowCount().incrementAndGet();
    	RowWrapper<T> rowWrapper = new RowWrapper<T>(rowNo, rowBean);
    	//设置行数据
    	int col = 0;
    	String filedName = null;
    	for(PropertyDescriptor pd: propDescs){
    	    filedName = pd.getName();
    	    Method method = pd.getWriteMethod();
    	    try {
        		Cell cell = row.getCell(col ++);
        		Object cellVal = parseCellValue(cell);
        		method.invoke(rowBean, cellVal);
        		parseStatus.getParseSuccessRowCount().incrementAndGet();
        		rowWrapper.setSuccess(true);
        		parseStatus.getParseSuccessRowQueue().offer(rowWrapper);
            } catch (Exception ignore) {
            	String column = filed2ColumnMap.get(filedName);
            	rowWrapper.setErrorMsg(MessageFormat.format("\'{0}\'列数据设置错误", column));
            	parseStatus.getErrorRowQueue().offer(rowWrapper);
            }
    	}
    }
    
    /**
     * 解析单元格数据
     * 1. 如果是CELL_TYPE_STRING类型，先解析Date，按照yyyy-MM-dd HH:mm:ss  yyyy/MM/dd HH:mm:ss。
     *    最后按照String处理。
     * 2. 如果是CELL_TYPE_NUMERIC类型，处理是否是Double或Integer
     * @param cell
     * @return
     */
    private Object parseCellValue(Cell cell) {
        Object cellVal = null;
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            cellVal = parseStringTypeCell(cell);
            break;
        case Cell.CELL_TYPE_NUMERIC:
            cellVal = cell.getNumericCellValue();
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            cell.getBooleanCellValue();
            break;
        case Cell.CELL_TYPE_BLANK:
            cellVal = "";
            break;
        default:
            cellVal = null;
            break;
        }
        return cellVal;
    }
    
    private Object parseStringTypeCell(Cell cell){
	Object cellObject = null;
	String cellVal = cell.getStringCellValue();
	String[] cellFormat = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"};
        SimpleDateFormat sdf = new SimpleDateFormat();
        for(String format: cellFormat){
            try {
		sdf.applyPattern(format);
		cellObject = sdf.parse(cellVal);
	    } catch (Exception ignore) {
	    }
        }
        if(cellObject != null){
            return cellObject;
        }
        return cellVal;
    }

}
