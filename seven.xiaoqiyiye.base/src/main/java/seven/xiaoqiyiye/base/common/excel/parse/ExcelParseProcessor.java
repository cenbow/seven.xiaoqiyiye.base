package seven.xiaoqiyiye.base.common.excel.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Queue;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import seven.xiaoqiyiye.base.common.excel.ExcelException;
import seven.xiaoqiyiye.base.common.fileupload.processor.AbstractUploadFileExecuteProcessor;

public class ExcelParseProcessor<T> extends AbstractUploadFileExecuteProcessor{

    InputStream is;
    
    Workbook wb;
    
    ExcelParseStatus<T> processStatus = new ExcelParseStatus<T>();
    
    SheetReadHandler<T> sheetReadHandler;
    
    ExcelRowProcessor<T> rowProcessor;
    
    public ExcelParseProcessor(List<String> columnNames, List<String> fieldNames, ExcelRowProcessor<T> rowProcessor){
		sheetReadHandler = new SheetReadHandler<T>(columnNames, fieldNames, this);
		this.rowProcessor = rowProcessor;
		executor.submit(new Worker());
    }
    
    public void process(FileItem fileItem) {
    	try {
    	    is = fileItem.getInputStream();
    	    wb = new XSSFWorkbook(is);
    	    Sheet sheet = wb.getSheetAt(0);
    	    sheetReadHandler.setSheet(sheet);
    	    sheetReadHandler.handle();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }

	public ExcelParseStatus<T> getProcessStatus() {
		return processStatus;
	}

	public ExcelRowProcessor<T> getRowProcessor(){
		return rowProcessor;
	}
	
	
	private class Worker implements Runnable{

		public void run() {
			Queue<RowWrapper<T>> successQueue = processStatus.getParseSuccessRowQueue();
			while(true){
				RowWrapper<T> rowWrapper = successQueue.poll();
				if(processStatus.isDone() && rowWrapper == null){
					break;
				}
				executor.submit(new RowProcessWorker(rowWrapper));
			}
		}
	}
	
	private class RowProcessWorker implements Runnable{

		RowWrapper<T> rowWrapper;
		
		RowProcessWorker(RowWrapper<T> rowWrapper){
			this.rowWrapper = rowWrapper;
		}

		public void run() {
			try {
				rowProcessor.process(rowWrapper);
				processStatus.getParseSuccessRowQueue().offer(rowWrapper);
			} catch (ExcelException e) {
				e.printStackTrace();
				rowWrapper.setSuccess(false);
				rowWrapper.setErrorMsg("数据处理错误");
				processStatus.getErrorRowQueue().offer(rowWrapper);
			}
		}
		
	}
	
}
