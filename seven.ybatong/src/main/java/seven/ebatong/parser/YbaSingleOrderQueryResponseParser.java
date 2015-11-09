package seven.ebatong.parser;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import seven.ebatong.SignUtils;
import seven.ebatong.YbaResponseParser;
import seven.ebatong.response.YbaSingleOrderQueryResponse;

/**
 * @description 订单查询响应解析器
 * 		返回结果responseString有如下情况：
 * 		1. 成功(SUCCESS)
 * 			<orderQuery>
				<status>SUCCESS</status>
				<charset>UTF-8</charset>
				<result>
					<outTradeNo>20150824261150-1217-8243</outTradeNo>
					<subject>charge</subject>
					<tradeNo>201508242330377123</tradeNo>
					<tradeStatus>TRADE_FINISHED</tradeStatus>
					<totalFee>0.01</totalFee>
				</result>
				<sign>36b58d5fac588d198390f493cab20348</sign>
			</orderQuery>
 *      2. 未找到(NOT_FOUND)
 * 			<orderQuery>
				<status>NOT_FOUND</status>
				<charset>UTF-8</charset>
				<sign>36b58d5fac588d198390f493cab20348</sign>
			</orderQuery>   	
 *      3. 错误(ERROR)
 * 			<orderQuery>
				<status>ERROR</status>
				<charset>UTF-8</charset>
			</orderQuery>  
 * @author linya 2015-8-24
 *
 */
public class YbaSingleOrderQueryResponseParser implements YbaResponseParser<YbaSingleOrderQueryResponse> {

	private static XPathFactory xPathFactory = XPathFactory.newInstance();
	
	private static final String XPATH_STATUS = "orderQuery/status";
	
	private static final String XPATH_SIGN = "orderQuery/sign";
	
	private static final String XPATH_CHARSET = "orderQuery/charset";
	
	private static final String XPATH_RESULT_OUTTRADENO = "orderQuery/result/outTradeNo";
	
	private static final String XPATH_RESULT_SUBJECT = "orderQuery/result/subject";
	
	private static final String XPATH_RESULT_TRADENO = "orderQuery/result/tradeNo";
	
	private static final String XPATH_RESULT_TRADESTATUS = "orderQuery/result/tradeStatus";
	
	private static final String XPAH_RESULT_TOTALFEE = "orderQuery/result/totalFee";
	
	
	private static final String STATUS_SUCCESS = "SUCCESS";
	
	private static final String STATUS_ERROR = "ERROR";
	
	public YbaSingleOrderQueryResponse parse(String responseString, boolean isSuccess) {
		
		YbaSingleOrderQueryResponse response = new YbaSingleOrderQueryResponse();
		
		try {
			if(isSuccess){
				System.out.println(responseString);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document root = builder.parse(new ByteArrayInputStream(responseString.getBytes()));
				String status = (String)evaluate(XPATH_STATUS, root, XPathConstants.STRING);
				String sign = (String)evaluate(XPATH_SIGN, root, XPathConstants.STRING);
				String charset = (String)evaluate(XPATH_CHARSET, root, XPathConstants.STRING);
				response.setSuccess(true);
				response.setStatus(status);
				response.setSign(sign);
				response.setInputCharset(charset);
				if(STATUS_SUCCESS.equals(status)){
					String outTradeNo = (String)evaluate(XPATH_RESULT_OUTTRADENO, root, XPathConstants.STRING);
					String subject = (String)evaluate(XPATH_RESULT_SUBJECT, root, XPathConstants.STRING);
					String tradeNo = (String)evaluate(XPATH_RESULT_TRADENO, root, XPathConstants.STRING);
					String tradeStatus = (String)evaluate(XPATH_RESULT_TRADESTATUS, root, XPathConstants.STRING);
					String totalFee = (String)evaluate(XPAH_RESULT_TOTALFEE, root, XPathConstants.STRING);
					response.setOutTradeNo(outTradeNo);
					response.setSubject(subject);
					response.setTradeNo(tradeNo);
					response.setTradeStatus(tradeStatus);
					response.setTotalFee(totalFee == null ? 0D : Double.valueOf(totalFee));
				}
				
				if(!STATUS_ERROR.equals(status)){
					boolean checked = checkSign(response);
					if(!checked){
						response.setSuccess(false);
						response.setErrorMessage("签名验证错误");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			response.setErrorMessage("请求结果解析异常");
		}
		
		return response;
	}

	private Object evaluate(String expression, Document root, QName type){
		try {
			return xPathFactory.newXPath().evaluate(expression, root, type);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @description 验签
	 * 	     把元素 status、 charset、outtradeNo、 subject、 tradeNo、 tradeStatus、 totalFee 
	 * 的非空值拼接起来（只拼接值，且就按此处列举的顺序拼接），后面再拼接商户自己的 key，然后对这个串
	 * 执行 MD5 加密，得到的密文如果与返回数据中的 sign 元素值相等，则表明返回数据合法
	 * @author linya 2015-8-24
	 */
	private boolean checkSign(YbaSingleOrderQueryResponse response){
		String sign = response.getSign();
		String signParam = def(response.getStatus()) + def(response.getInputCharset()) + def(response.getOutTradeNo())
				+ def(response.getSubject()) + def(response.getTradeNo()) + def(response.getTradeStatus())
				+ def(response.getTotalFee());
		return SignUtils.checkSign(sign, signParam);
	}
	
	private String def(Object val){
		return val == null ? "" : val.toString();
	}
	
}
