package seven.xiaoqiyiye.base.common.fileupload;


import org.apache.commons.fileupload.FileItem;

public interface UploadFileProcessor {

    void process(FileItem fileItem);
    
    UploadFileStatus getProcessStatus();

}
