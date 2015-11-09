package seven.xiaoqiyiye.base.common.excel.exports;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import seven.xiaoqiyiye.base.common.excel.ExcelException;


/**
 * <p>
 * 示例：
 * SheetBuilder.newBuilder()
 * 		.setShowNo(true)		//optional
 * 		.setSheetName(sheetName)	//optional
 * 		.setColumnNames(columnNames)	//required
 * 		.setFieldNames(fieldNames)	//required
 * 		.setData(data)			//required
 * 		.build()			//构建Excel的第一个Sheet
 * 		.setShowNo(true)				
 * 		.setSheetName(sheetName)		
 * 		.setColumnNames(columnNames)	
 * 		.setFieldNames(fieldNames)		
 * 		.setData(data)					
 * 		.build()			//构建Excel的第二个Sheet
 * 		.export(fileName);		//导出文件，指定导出的文件名
 * </p>
 * @author linya Nov 19, 2014 6:59:12 PM
 */
public class SheetBuilder {
	
    private static final String DEFAULT_SHEETNAME = "Sheet0";
	
    ExcelHandler handler;
	
    boolean showNo;	//是否显示编号

    List<String> columnNames = new ArrayList<String>();
	
    List<String> fieldNames = new ArrayList<String>();
	
    List<?> data;
	
    String sheetName;
	
    private SheetBuilder(){
	
    }
	
    public static SheetBuilder newBuilder(){
	SheetBuilder builder = new SheetBuilder();
	builder.handler = new ExcelHandler();
	return builder;
    }
	
	
    /**
     * 每次build之后都需要重新设置参数，否则，抛出异常
     * @author linya Nov 19, 2014 6:58:10 PM
     * @return
     * @throws ExcelException
     */
    public SheetBuilder build() throws ExcelException{
	//校验参数设置
	if(columnNames == null || fieldNames == null || data == null){
	    throw new ExcelException("MultiSheetBuilder 失败");
	}
	//处理Excel
	SheetWriteHandler sheetHandler = new SheetWriteHandler(columnNames, fieldNames, data, showNo);
	if(sheetName == null){
	    sheetName = DEFAULT_SHEETNAME;
	}
	handler.executeSheet(sheetName, sheetHandler);
	clear();
	return this;
    }
	
    private void clear(){
	showNo = false;
	columnNames = null;
	fieldNames = null;
	data = null;
	sheetName = DEFAULT_SHEETNAME;
    }

    public void export(String fileName, HttpServletRequest request, HttpServletResponse response){
	ServletOutputStream sos = null;
	try{
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8"); 
	    if(fileName == null || "".equals(fileName)){
		fileName = "ExcelExport_" + System.currentTimeMillis();
	    }
	    fileName = java.net.URLDecoder.decode(fileName,"UTF-8");
	    fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
	    response.addHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
	    response.setContentType("application/msexcel");
	    sos =  response.getOutputStream();
	    handler.write(sos);
	}catch(Exception e){
	    e.printStackTrace();
	}finally{
	    try {
		handler.close();
		if(sos != null){
		    sos.flush();
		    sos.close();
		}
	    } catch (Exception e) {}
	}
    }
	
    public void export(OutputStream os){
	handler.write(os);
	System.out.println("++++ Excel导出完成!");
    }

	
    public SheetBuilder setShowNo(boolean showNo) {
    	this.showNo = showNo;
    	return this;
    }

	
    public SheetBuilder setColumnNames(String[] columnNames) {
    	this.columnNames = Arrays.asList(columnNames);
    	return this; 
    }

	
    public SheetBuilder setFieldNames(String[] fieldNames) {
    	this.fieldNames = Arrays.asList(fieldNames);
    	return this;
    }

	
    public SheetBuilder setData(List<?> data) {
    	this.data = data;
    	return this;
    }

	
    public SheetBuilder setSheetName(String sheetName) {
    	this.sheetName = sheetName;
    	return this;
    }

}
