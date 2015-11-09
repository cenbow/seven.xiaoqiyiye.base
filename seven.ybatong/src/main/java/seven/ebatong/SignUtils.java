package seven.ebatong;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 贝付签名(ebatong)
 * 签名规则:
 * 1. 升序排序参数
 * 2. 排序参数+加密Key 进行加密处理，得到签名sign
 * 3. url+排序参数+sign就是得到签名后的url
 * @see SignUtils#signUrl(String, Map)
 * 
 * 校验签名：
 * 	1.从参数中删除sign参数
 *  2.将剩余的参数进行签名处理，得到待验证的签名
 *  3.将删除的sign和新的sign比较，如果相同则签名校验通过
 * @see SignUtils#checkSign(Map)
 * @author linya 2015-7-23
 *
 */
public class SignUtils {

	private static Logger logger = Logger.getLogger(SignUtils.class);
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * @description 获取被签名后的url信息
	 *          URL+参数+签名，也就是说 url + param + sign
	 * @author linya 2015-7-23
	 * @return 签名后的url   eg:  http://12.23.34.45/xxx?p1=v1&p2=v2&p3=v3&sign=vsign
	 */
	public static String signUrl(String url, Map<String, Object> paramMap){
		//签名参数需要排序，但不能编码，签名参数 p1=v1&p2=v2&p3=v3
		String sortedParam = sortRequestParam(paramMap, true, false);
		String sign = sign(sortedParam);
		//URL参数不需要排序，但需要编码
		sortedParam = sortRequestParam(paramMap, false, true);
		return url + "?" + sortedParam + "&sign=" + sign;
	}
	
	
	/**
	 * @description 升序排序参数，并返回待签名的字符串
	 * @author linya 2015-7-23
	 * @param paramMap 请求参数
	 * @param sorted 是否排序
	 * @param encode 是否编码
	 * @return 返回待签名的参数字符串    eg: p1=v1&p2=v2&p3=v3
	 */
	private static String sortRequestParam(Map<String, Object> paramMap, boolean sorted, boolean encode){
		Map<String, Object> map = null;
		if(sorted){
			map = new TreeMap<String, Object>(new Comparator<String>() {
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
		}else{
			map = new HashMap<String, Object>();
		}
		map.putAll(paramMap);
		
		StringBuffer sortedParam = new StringBuffer(500);
		String currentParam = null;
		try {
			for(Map.Entry<String, Object> f: map.entrySet()){
				String key = f.getKey();
				Object val = f.getValue();
				currentParam = key;
				String value = null;
				if(val != null){
					if(val instanceof Date){
						value = sdf.format((Date)val);
					}else if(val instanceof Calendar){
						value = sdf.format(((Calendar)val).getTime());
					}else{
						value = val.toString();
					}
				}
				if(StringUtils.isBlank(value)){
					value = "";
				}
				sortedParam.append("&" + key + "=" + (encode ? URLEncoder.encode(value, YbaConfigs.INPUT_CHARSET)  : value));
			}
			
			//去点前面多余的&
			if(sortedParam.length() > 0){
				sortedParam.deleteCharAt(0);
			}
		} catch (Exception e) {
			logger.info("++++ 参数发生异常:" + currentParam);
			e.printStackTrace();
		}
		
		return sortedParam.toString();	
	}
	
	
	/**
	 * @description 
	 *     1. 排序参数+加密Key
	 *     2. 按照加密类型得到加密后的sign
	 * @author linya 2015-7-23
	 * @return 返回sign
	 */
	public static String sign(String sortedParam){
		//排序参数+加密Key
		String paramAndKey = sortedParam + YbaConfigs.SIGN_KEY;
		
		//加密处理
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(YbaConfigs.SIGN_TYPE);
			digest.update(paramAndKey.getBytes(YbaConfigs.INPUT_CHARSET));
			byte[] resultBytes = digest.digest();
			return toHex(resultBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    private static String toHex(byte[] byteArray) {
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            int val = byteArray[i] & 0xFF;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
    
    /**
     * @description 签名验证
     * @author linya 2015-7-23
     */
    public static boolean checkSign(String sign, Map<String, Object> paramMap){
    	String oldSign = null;
    	if(StringUtils.isBlank(sign)){
    		oldSign = paramMap.get("sign") == null  ? null : paramMap.get("sign").toString();
    	}
    	if(StringUtils.isBlank(oldSign)){
    		return false;
    	}
    	paramMap.remove("sign");
    	String sortedParam = sortRequestParam(paramMap, true, false);
    	String newSign = sign(sortedParam);
    	return oldSign.endsWith(newSign);
    }
    
    public static boolean checkSign(Map<String, Object> paramMap){
    	return checkSign(null, paramMap);
    }
    
    public static boolean checkSign(String sign, String signParam){
    	String checkedSign = sign(signParam);
    	if(sign == null || checkedSign == null){
    		return false;
    	}
    	return checkedSign.equals(sign);
    }
}
