package seven.ebatong.parser;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import seven.ebatong.ParamName;
import seven.ebatong.SignUtils;
import seven.ebatong.YbaResponseParser;
import seven.ebatong.response.YbaNotifyUrlResponse;

import com.alibaba.fastjson.JSON;


public class YbaNotifyUrlResponseParser implements YbaResponseParser<YbaNotifyUrlResponse>{

	private static Logger log = Logger.getLogger(YbaNotifyUrlResponseParser.class);
	
	private static final String CONTENT_TYPE_JSON = "application/json"; 
	
	public YbaNotifyUrlResponse parse(String responseString, boolean isSuccess) {
		YbaNotifyUrlResponse response = new YbaNotifyUrlResponse();
		//response.parseJson(responseString);
		return response;
	}

	public static YbaNotifyUrlResponse parse(HttpServletRequest request) {
		
		YbaNotifyUrlResponse response = null;
		
		if(CONTENT_TYPE_JSON.equalsIgnoreCase(request.getContentType())){
			try {
				String responseJson = IOUtils.toString(request.getInputStream());
				boolean checked = checkSign(responseJson);
				if(!checked){
					response = new YbaNotifyUrlResponse();
					response.setSuccess(false);
					response.setErrorMessage("签名校验错误");
					return response;
				}
				log.info("++++ notify_url返回json信息:" + responseJson);
				YbaNotifyUrlResponseParser parser = new YbaNotifyUrlResponseParser();
				response = parser.parse(responseJson, true);
			} catch (Exception e) {
				log.info("++++ NotifyUrl响应解析异常，异常消息:" + e.getMessage());
			}
		}else{
			response = new YbaNotifyUrlResponse();
			boolean checked = checkSign(request);
			if(!checked){
				response.setSuccess(false);
				response.setErrorMessage("签名校验错误");
				return response;
			}
			Field[] allFields = response.getClass().getFields();
			try {
				for(Field f: allFields){
					ParamName paramName = f.getAnnotation(ParamName.class);
					String param;
					if(paramName != null){
						param = paramName.value();
					}else{
						param = f.getName();
					}
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), response.getClass());
					Method method = pd.getWriteMethod();
					method.invoke(response, request.getParameter(param));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean checkSign(String responseJson){
		Map<String, Object> paramMap = (Map<String, Object>)JSON.parseObject(responseJson, Map.class);
		return SignUtils.checkSign(paramMap);
	}
	
	private static boolean checkSign(HttpServletRequest request){
		Map<String, String[]> requestParam = request.getParameterMap();
		Map<String, Object> paramMap = new HashMap<String, Object>(requestParam.size());
		for(Map.Entry<String, String[]> entry: requestParam.entrySet()){
			String key = entry.getKey();
			String[] val = entry.getValue();
			if(val == null || val.length == 0){
				paramMap.put(key, "");
			}else{
				paramMap.put(key, val[0]);
			}
		}
		return SignUtils.checkSign(paramMap);
	}
}
