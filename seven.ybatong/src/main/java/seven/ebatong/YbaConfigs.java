package seven.ebatong;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class YbaConfigs {

	private static Properties prop;
	
	private static boolean inited;
	
	//商户号
	public static String PARTNER_ID = get("partner_id");
	
	//加密Key
	public static String SIGN_KEY = get("sign_key");
	
	//签名方式
	public static String SIGN_TYPE = get("sign_type");
	
	//签名编码
	public static String INPUT_CHARSET = get("input_charset", "utf-8");
	
	//支付网关
	public static String PAY_GATEWAY = get("pay_gateway");
	
	//获取时间戳地址
	public static String GATEWAY = get("timestamp_gateway");
	
	//获取验证码地址
	public static String URL_VERIFICATION_CODE = get("url_verification_code");
	
	//支付查询结果地址
	public static String QUERY_URL = get("query_url");
	
	//通知地址
	public static String NOTIFY_URL =get("notify_url");
	
	//返回地址
	public static String RETURN_URL = get("return_url");
	
	private static void init(){
		if(prop == null){
			InputStream is = YbaConfigs.class.getResourceAsStream("yibatongConfig.properties");
			if(is != null){
				prop = new Properties();
				try {
					prop.load(is);
					inited = true;
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				throw new RuntimeException("++++ 没有获取到获取资源文件yibatongConfig.properties");
			}	
		}
	}
	
	private static String get(String key){
		if(!inited){
			init();
		}
		return prop.getProperty(key);
	}
	
	private static String get(String key, String defaultValue){
		if(!inited){
			init();
		}
		return prop.getProperty(key,defaultValue);
	}

}
