package seven.ebatong.request;

import seven.ebatong.ParamName;
import seven.ebatong.YbaConfigs;

public class YbaTimestampRequest extends AbstractYbaRequest{

	@ParamName("input_charset")
	protected String inputCharset;
	
	public String getRequestUrl() {
		return YbaConfigs.GATEWAY;
	}

	@Override
	public void setService() {
		super.service = "query_timestamp";
	}

	@Override
	protected void initParam() {
		super.initParam();
		this.inputCharset = YbaConfigs.INPUT_CHARSET;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}
	
}
