package seven.ebatong.request;

import seven.ebatong.ParamName;
import seven.ebatong.YbaConfigs;

public class YbaSingleOrderQueryRequest extends AbstractYbaRequest {

	@ParamName("out_trade_no")
	private String outOrderNo;
	
	public String getRequestUrl() {
		return YbaConfigs.GATEWAY;
	}

	@Override
	protected void setService() {
		this.service = "single_direct_query";
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	
}
