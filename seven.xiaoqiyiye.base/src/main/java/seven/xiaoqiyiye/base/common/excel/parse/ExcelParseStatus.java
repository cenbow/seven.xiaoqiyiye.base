package seven.xiaoqiyiye.base.common.excel.parse;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


import seven.xiaoqiyiye.base.common.fileupload.UploadFileStatus;

public class ExcelParseStatus<T> extends UploadFileStatus{

	//解析的总行数
	AtomicInteger parseRowCount = new AtomicInteger(0);
	
	//解析成功的行数
	AtomicInteger parseSuccessRowCount = new AtomicInteger(0);
	
	//处理成功的行数
	AtomicInteger processSuccessRowCount = new AtomicInteger(0);
	
	//错误的行数（解析错误+处理错误）
	AtomicInteger errorRowCount = new AtomicInteger(0);
	
	//解析成功的队列
	Queue<RowWrapper<T>> parseSuccessRowQueue = new LinkedBlockingQueue<RowWrapper<T>>();
	
	//处理成功的队列
	Queue<RowWrapper<T>> processSuccessRowQueue = new LinkedBlockingQueue<RowWrapper<T>>();
	
	//错误的队列
	Queue<RowWrapper<T>> errorRowQueue = new LinkedBlockingQueue<RowWrapper<T>>();
	

	public AtomicInteger getParseRowCount() {
		return parseRowCount;
	}

	public AtomicInteger getParseSuccessRowCount() {
		return parseSuccessRowCount;
	}

	public AtomicInteger getErrorRowCount() {
		return errorRowCount;
	}

	public Queue<RowWrapper<T>> getParseSuccessRowQueue() {
		return parseSuccessRowQueue;
	}

	public Queue<RowWrapper<T>> getProcessSuccessRowQueue() {
		return processSuccessRowQueue;
	}

	public Queue<RowWrapper<T>> getErrorRowQueue() {
		return errorRowQueue;
	}
	
	
}
