package seven.xiaoqiyiye.base.common.fileupload.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItem;

import seven.xiaoqiyiye.base.common.fileupload.UploadFileProcessor;
import seven.xiaoqiyiye.base.common.fileupload.UploadFileStatus;

public class UploadFileDiskProcessor implements UploadFileProcessor {

    private static final int BUFFER_SIZE = 4096;

    private OutputStream os;
    
    private String filePath;
    
    private int bufferSize = BUFFER_SIZE;
    
    private UploadFileStatus processorStatus = new UploadFileStatus();
    
    public UploadFileDiskProcessor(){
	
    }
    
    public UploadFileDiskProcessor(String filePath){
    	this.filePath = filePath;
    }
    
    private void initFile(String filename) throws IOException, FileNotFoundException{
    	File file = new File(filePath + File.separator + filename);
    	if(!file.exists()){
    	    file.createNewFile();
    	}
    	os = new FileOutputStream(file);
    }
    
    public void process(FileItem fileItem) {
    	InputStream is = null;
        try{
            initFile(fileItem.getName());
            is = fileItem.getInputStream();
            byte buffer[] = new byte[bufferSize];
            int bytes_read;
            while((bytes_read = is.read(buffer)) != -1){
            	os.write(buffer, 0, bytes_read);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
		if(is != null){
		    is.close();
		}
		if(os != null){
		    os.close();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

	public UploadFileStatus getProcessStatus() {
		return processorStatus;
	}
    
}
