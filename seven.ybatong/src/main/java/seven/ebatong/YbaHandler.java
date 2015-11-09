package seven.ebatong;

import java.io.IOException;
import java.util.Map;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class YbaHandler<T extends YbaResponse> {

	private static Logger log = Logger.getLogger(YbaHandler.class);
	
	YbaResponseParser<T> parser;
	
	public YbaHandler(YbaResponseParser<T> parser){
		this.parser = parser;
	}
	
	
	/**
	 * @description 
	 * @author linya 2015-7-23
	 * @param request 请求对象
	 */
	public T send(YbaRequest request){
		String url = request.getRequestUrl();
		Map<String, Object> requestParam = request.paramMap();
		return send(url, requestParam);
	}
	
	/**
	 * @author linya 2015-7-23
	 * @param url 请求url
	 * @param requestParam 请求参数
	 * @return 返回响应对象
	 */
	public T send(String url, Map<String, Object> requestParam){
		String signUrl = SignUtils.signUrl(url, requestParam);
		ResponseWrapper responseWrapper = requestGet(signUrl);
		T response = parser.parse(responseWrapper.responseResult, responseWrapper.isSuccess);
		return response;
	}
	
	private static ResponseWrapper requestGet(String url){
		String resp = null;
		boolean isSuccess = false;
		log.info("++++ 请求URL  " + url);
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				resp = EntityUtils.toString(entity);
			}
			isSuccess = (HttpStatus.SC_OK == response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			log.info("++++ 请求错误，URL：" + url + ",异常消息：" + e.getMessage());
		} catch (IOException e) {
			log.info("++++ 请求错误，URL：" + url + ",异常消息：" + e.getMessage());
		}
		
		return new ResponseWrapper(resp, isSuccess);
	}
	
	private static class ResponseWrapper{
		String responseResult;
		boolean isSuccess;
		ResponseWrapper(String responseResult, boolean isSuccess){
			this.responseResult = responseResult;
			this.isSuccess = isSuccess;
		}
	}
}
