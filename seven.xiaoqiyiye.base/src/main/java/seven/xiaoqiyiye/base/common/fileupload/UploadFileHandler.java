package seven.xiaoqiyiye.base.common.fileupload;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import seven.xiaoqiyiye.base.common.fileupload.processor.AbstractUploadFileExecuteProcessor;



public class UploadFileHandler {

    private static final String UPLOAD_FILE_SESSION = "upload_file_session";
    
    private static final int DEFAULT_CORE_POOL_SIZE = 20;
    
    private static int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    
    private static UploadFileHandler defaultHandler;

    private ExecutorService executor;
    
    private UploadFileHandler(){
    	this(Executors.newFixedThreadPool(corePoolSize));
    }
    
    private UploadFileHandler(ExecutorService executor){
    	this.executor = executor;
    }
    
    public static UploadFileHandler getInstance(){
    	if(defaultHandler == null){
    	    defaultHandler = new UploadFileHandler();
    	}
    	return defaultHandler;
    }
    
    public static UploadFileStatus upload(HttpServletRequest request, UploadFileProcessor processor) throws Exception{
    	UploadFileStatus status = getInstance().parseRequest(request, processor);
    	System.out.println("+++ end upload.");
    	return status;
    }
    
    public static UploadFileStatus status(HttpServletRequest request){
    	UploadFileStatus status = (UploadFileStatus) request.getSession().getAttribute(UPLOAD_FILE_SESSION);
    	if(status.isDone()){
    	    request.getSession().removeAttribute(UPLOAD_FILE_SESSION);
    	}
    	return status;
    }
    
    @SuppressWarnings("unchecked")
    private UploadFileStatus parseRequest(HttpServletRequest request, UploadFileProcessor processor) throws FileUploadException{
    	HttpSession session = request.getSession();
    	if(processor instanceof AbstractUploadFileExecuteProcessor){
    		((AbstractUploadFileExecuteProcessor)processor).setExecutor(executor);
    	}
    	UploadFileStatus uploadFileStatus = processor.getProcessStatus();
    	uploadFileStatus.totalSize = request.getContentLength();
    	session.setAttribute(UPLOAD_FILE_SESSION, uploadFileStatus);
    	UploadFileItemFactory factory = new UploadFileItemFactory(uploadFileStatus);
    	ServletFileUpload servletUpload = new ServletFileUpload(factory);
    	List<FileItem> items = servletUpload.parseRequest(request);
    	for(Iterator<FileItem> i = items.iterator(); i.hasNext();){
    	    UploadFileItem fileItem = (UploadFileItem)i.next();
    	    if(!fileItem.isFormField()){
    	    	executor.submit(new ProcessTask(processor, fileItem));
    	    }
    	}
    	return uploadFileStatus;
    }
    
    private class ProcessTask implements Runnable{

    	UploadFileProcessor processor;
    	UploadFileItem fileItem;
    	
    	ProcessTask(UploadFileProcessor processor, UploadFileItem fileItem){
    	    this.processor = processor;
    	    this.fileItem = fileItem;
    	}
    	
    	public void run() {
    	    UploadFileStatus status = fileItem.getUploadFileStatus();
    	    status.start();
    	    System.out.println("++++ statr upload file:" + fileItem.getName() + ", size:" + displaySize(fileItem.getSize()));
    	    processor.process(fileItem);
    	    status.done();
    	    System.out.println("++++ end upload file:" + fileItem.getName());
    	    fileItem.delete();
    	}
    }
    
    private static String displaySize(long size){
	String[] dw = {"B", "KB", "MB", "GB", "TB"};
	int i = 0;
	double temp = size;
	while(temp > 1024){
	    temp = temp/1024;
	    i ++;
	    if(i == dw.length - 1){
		break;
	    }
	}
	String display = (temp * 100)/100 + dw[i];
	return display;
    }
    
}
