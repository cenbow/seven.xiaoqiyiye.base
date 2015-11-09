package seven.xiaoqiyiye.base.common.excel.exports;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import seven.xiaoqiyiye.base.common.excel.ExcelException;


public class ExcelHandler {
	
    Workbook wb;

    ExcelHandler(){
	wb = new XSSFWorkbook();
    }

    void executeSheet(String sheetName, SheetWriteHandler sheetHandler) throws ExcelException{
	Sheet sheet = wb.createSheet(sheetName);
	sheetHandler.setSheet(sheet);
	sheetHandler.handle();
    }
	
    void write(OutputStream os){
	try {
	    wb.write(os);
        } catch (IOException e) {
	    e.printStackTrace();
        }
    }
	
    void close(){
	try {
	    if(wb != null){
		wb.close();
	    }
        } catch (IOException e) {
	    e.printStackTrace();
        }
    }
}
