package seven.ebatong.parser;

import seven.ebatong.YbaResponseParser;
import seven.ebatong.response.YbaVerificationCodeResponse;

public class YbaVerificationCodeResponseParser implements YbaResponseParser<YbaVerificationCodeResponse> {

	/**
	 * @description 
	 * @author linya 2015-7-23
	 * @param responseString  {"sign":"02a2d486df6848343259d6b3bcb736c7","amount":"0.01","result":"T","token":"201411251624411790","sign_type":"MD5","error_message":"","input_charset":"UTF-8","service":"ebatong_mp_dyncode","partner":"201312311646307244","out_trade_no":"20141125162234-10002589","customer_id":""}
	 * @param isSuccess
	 */
	public YbaVerificationCodeResponse parse(String responseString,
			boolean isSuccess) {
		YbaVerificationCodeResponse response = new YbaVerificationCodeResponse();
		response.setSuccess(isSuccess);
		if(isSuccess && responseString != null){
			//response.parseJson(responseString);
		}
		return response;
	}

	
}
