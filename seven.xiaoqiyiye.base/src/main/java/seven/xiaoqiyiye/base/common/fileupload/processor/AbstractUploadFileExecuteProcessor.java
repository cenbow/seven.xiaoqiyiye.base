package seven.xiaoqiyiye.base.common.fileupload.processor;

import java.util.concurrent.ExecutorService;

import seven.xiaoqiyiye.base.common.fileupload.UploadFileProcessor;

public abstract class AbstractUploadFileExecuteProcessor implements UploadFileProcessor {

	protected ExecutorService executor;
	
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}
	
}
