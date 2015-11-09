package seven.ebatong.response;


public class YbaTimestampResponse extends AbstractYbaResponse {

	private String timestamp;
	
	private String sign;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
