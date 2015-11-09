package seven.xiaoqiyiye.base.common.fileupload;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import seven.xiaoqiyiye.base.common.fileupload.processor.UploadFileDiskProcessor;

@Controller
@RequestMapping("/uploadfile")
public class UploadFileController {

	@RequestMapping("/")
	public String upload() {
		return "/uploadfile";
	}

	/**
	 * @description 请求上传文件
	 * @author linya
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public UploadFileStatus upload(HttpServletRequest request) {
		UploadFileStatus status = null;
		System.out.println("++++ request uoload");
		try {
			UploadFileDiskProcessor processor = new UploadFileDiskProcessor(
					"E:\\upload");
			status = UploadFileHandler.upload(request, processor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * @description 监听上次文件的状态（页面上传请求后定时监听状态）
	 * @author linya
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/status", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public UploadFileStatus status(HttpServletRequest request) {
		UploadFileStatus status = null;
		System.out.println("++++ request status");
		try {
			status = UploadFileHandler.status(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
