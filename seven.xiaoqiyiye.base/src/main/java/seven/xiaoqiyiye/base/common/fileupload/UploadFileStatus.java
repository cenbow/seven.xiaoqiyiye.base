package seven.xiaoqiyiye.base.common.fileupload;

/**
 * 文件上传状态类
 * @author linya 2015-8-4
 * 
 */
public class UploadFileStatus {

	private static final int STATUS_NONE = 0; // 初始状态

	private static final int STATUS_START = 1; // 开始上传

	private static final int STATUS_READING = 2; // 上传中

	private static final int STATUS_DONE = 3; // 上传结束

	private static final int STATUS_ERROR = -1; // 错误

	long totalSize; // 文件总大小

	long bytesRead; // 已读字节

	long startTime; // 开始时间

	long doneTime; // 结束时间

	int status; // 状态

	public UploadFileStatus() {
		status = STATUS_NONE;
	}

	public void start() {
		status = STATUS_START;
		startTime = System.currentTimeMillis();
	}

	public void bytesRead(int i) {
		bytesRead += i;
		status = STATUS_READING;
	}

	public void error(String s) {
		status = STATUS_ERROR;
	}

	public void done() {
		bytesRead = totalSize;
		status = STATUS_DONE;
		doneTime = System.currentTimeMillis();
	}

	public boolean isDone() {
		return status == STATUS_DONE;
	}

	public long getUploadTime() {
		if (status == STATUS_DONE) {
			return doneTime - startTime;
		} else if (status == STATUS_READING) {
			return System.currentTimeMillis() - startTime;
		} else {
			return 0L;
		}
	}

	public long getTotalSize() {
		return totalSize;
	}

	public long getBytesRead() {
		return bytesRead;
	}

	public int getStatus() {
		return status;
	}

}
