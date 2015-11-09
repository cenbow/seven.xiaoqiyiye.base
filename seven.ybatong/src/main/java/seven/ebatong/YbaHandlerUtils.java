package seven.ebatong;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import seven.ebatong.consts.BankCode;
import seven.ebatong.consts.PaymentType;
import seven.ebatong.parser.YbaSingleOrderQueryResponseParser;
import seven.ebatong.parser.YbaTimestampResponseParser;
import seven.ebatong.request.YbaPayRequest;
import seven.ebatong.request.YbaSingleOrderQueryRequest;
import seven.ebatong.request.YbaTimestampRequest;
import seven.ebatong.response.YbaSingleOrderQueryResponse;
import seven.ebatong.response.YbaTimestampResponse;


public class YbaHandlerUtils {

	
	
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddmmhhss");
	
	/**
	 * 获取时间戳,用于反钓鱼安全
	 * @description 
	 * @author linya 2015-7-23
	 */
	public static String getTimestamp(){
		YbaTimestampRequest request = new YbaTimestampRequest();
		request.setPartner(YbaConfigs.PARTNER_ID);
		YbaHandler<YbaTimestampResponse> handler = new YbaHandler<YbaTimestampResponse>(new YbaTimestampResponseParser());
		YbaTimestampResponse response = handler.send(request);
		String timestamp = response.getTimestamp();
		return timestamp;
	}
	
	/**
	 * 获取支付请求URL
	 * @description 
	 * @author linya 2015-7-23
	 */
	public static String getPayUrl(YbaPayRequest request, YbaHandleCallback<YbaPayRequest> callback){
		String url = request.getRequestUrl();
		Map<String, Object> map = request.paramMap();
		String signUrl = SignUtils.signUrl(url, map);
		if(callback != null){
			callback.call(request, null);
		}
		System.out.println("++++ payUrl: " + signUrl);
		return signUrl;
	}
	
	public static String getPayUrl(Double totalFee, String subject, String bankCode, String invokeIp, YbaHandleCallback<YbaPayRequest> callback){
		YbaPayRequest request = new YbaPayRequest();
		request.setTotalFee(totalFee);
		request.setSubject(subject);
		request.setBankCode(BankCode.getType(bankCode));
		request.setInvokeIp(invokeIp);
		String antiPhishingKey = getTimestamp();
		request.setAntiPhishingKey(antiPhishingKey);
		request.setOutTradeNo(generateOrderNo());
		request.setPaymentType(PaymentType.one);
		return getPayUrl(request, callback);
	}
	
	private static String generateOrderNo(){
        String timeStamp = sf.format(new Date());
        Random random = new Random();
        int random1 = random.nextInt(10000);
        int random2 = random.nextInt(10000); 
        return timeStamp + "-" + random1 + "-" + random2;
	}
	
	public static YbaSingleOrderQueryResponse queryOrder(String orderNo){
		YbaSingleOrderQueryRequest request = new YbaSingleOrderQueryRequest();
		request.setOutOrderNo(orderNo);
		request.setPartner(YbaConfigs.PARTNER_ID);
		YbaHandler<YbaSingleOrderQueryResponse> handler = new YbaHandler<YbaSingleOrderQueryResponse>(new YbaSingleOrderQueryResponseParser());
		YbaSingleOrderQueryResponse response = handler.send(request);
		return response;
	}
}
