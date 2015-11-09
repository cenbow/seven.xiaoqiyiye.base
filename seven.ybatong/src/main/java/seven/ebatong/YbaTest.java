package seven.ebatong;

import seven.ebatong.consts.BankCode;
import seven.ebatong.request.YbaPayRequest;



public class YbaTest {

	public static void main(String[] args) {
		//testPayUrl();
		testQuery();
	}
	
	private static void testTimestamp(){
		YbaHandlerUtils.getTimestamp();
	}
	
	private static void testPayUrl(){
		YbaHandlerUtils.getPayUrl(0.01, "charge", BankCode.CMB_B2C.toString(), "127.0.0.1", new YbaHandleCallback<YbaPayRequest>() {
			public void call(YbaPayRequest request, Object attachment) {
				System.out.println(request.getOutTradeNo());
			}
		});
	}
	
	private static void testQuery(){
		//20150824041014-002341-1703
		//20150824261150-1217-8243
		YbaHandlerUtils.queryOrder("20150825481252-3529-8706");		
	}
}
