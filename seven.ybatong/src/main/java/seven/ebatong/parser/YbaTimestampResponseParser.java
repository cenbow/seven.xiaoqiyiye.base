package seven.ebatong.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import seven.ebatong.SignUtils;
import seven.ebatong.YbaResponseParser;
import seven.ebatong.response.YbaTimestampResponse;

public class YbaTimestampResponseParser implements YbaResponseParser<YbaTimestampResponse> {

	XPathFactory xpathFactory = XPathFactory.newInstance();
	
	private static final String XPATH_IS_SUCCESS = "ebatong/is_success";
	
	private static final String XPATH_TIMESTAMP = "ebatong/response/timestamp/encrypt_key";
	
	private static final String XPAH_SIGN = "ebatong/sign";
	
	public YbaTimestampResponse parse(String responseString, boolean isSuccess) {
		YbaTimestampResponse response = new YbaTimestampResponse();
		try {
			if(isSuccess){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document root = builder.parse(new ByteArrayInputStream(responseString.getBytes()));
				
				String success = (String)xpathFactory.newXPath().evaluate(XPATH_IS_SUCCESS, root, XPathConstants.STRING);
				String timestamp = (String)xpathFactory.newXPath().evaluate(XPATH_TIMESTAMP, root, XPathConstants.STRING);
				String sign = (String)xpathFactory.newXPath().evaluate(XPAH_SIGN, root, XPathConstants.STRING);
				//签名验证
				boolean check = SignUtils.checkSign(sign, timestamp);
				response.setSuccess(check);
				if(check){
					response.setResult(success);
					response.setTimestamp(timestamp);
					response.setSign(sign);
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return response;
	}
	
}
