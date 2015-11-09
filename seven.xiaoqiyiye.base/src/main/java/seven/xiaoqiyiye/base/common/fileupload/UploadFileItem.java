package seven.xiaoqiyiye.base.common.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;

/**
 * UploadFileItem类表示上传的具体文件，uploadFileStatus属性表
 * @author linya
 *
 */
public class UploadFileItem extends DiskFileItem {

	private static final long serialVersionUID = 1L;

	UploadFileStatus uploadFileStatus;

	OutputStream os;

	public UploadFileItem(String fieldName, String contentType,
			boolean isFormField, String fileName, int sizeThreshold,
			File repository) {
		super(fieldName, contentType, isFormField, fileName, sizeThreshold,
				repository);
	}

	public void setUploadFileStatus(UploadFileStatus uploadFileStatus) {
		this.uploadFileStatus = uploadFileStatus;
	}

	public UploadFileStatus getUploadFileStatus() {
		return uploadFileStatus;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return new OutputStreamWrapper(super.getOutputStream(),
				uploadFileStatus);
	}

	private static class OutputStreamWrapper extends OutputStream {

		OutputStream os;

		UploadFileStatus status;

		private OutputStreamWrapper(OutputStream os, UploadFileStatus status) {
			this.os = os;
			this.status = status;
		}

		public void write(byte b[], int off, int len) throws IOException {
			os.write(b, off, len);
			status.bytesRead(len - off);
		}

		public void write(byte[] bytes) throws IOException {
			os.write(bytes);
			status.bytesRead(bytes.length);
		}

		public void write(int b) throws IOException {
			os.write(b);
			status.bytesRead(1);
		}

		public void close() throws IOException {
			if (os != null) {
				os.close();
			}
		}

		public void flush() throws IOException {
			if (os != null) {
				os.flush();
			}
		}
	}

}
