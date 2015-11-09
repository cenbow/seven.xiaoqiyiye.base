package seven.xiaoqiyiye.base.common.fileupload;

import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

/**
 * UploadFileItemFactory是UploadFileItem的工厂类，用于创建
 * @author linya
 */
public class UploadFileItemFactory extends DiskFileItemFactory {

	private UploadFileStatus uploadFileStatus;

	public UploadFileItemFactory(UploadFileStatus uploadFileStatus) {
		this.uploadFileStatus = uploadFileStatus;
	}

	public UploadFileItemFactory(int sizeThreshold, File repository) {
		super(sizeThreshold, repository);
	}

	@Override
	public FileItem createItem(String fieldName, String contentType,
			boolean isFormField, String fileName) {
		UploadFileItem fileItem = new UploadFileItem(fieldName, contentType,
				isFormField, fileName, getSizeThreshold(), getRepository());
		fileItem.setUploadFileStatus(uploadFileStatus);
		return fileItem;
	}

}
