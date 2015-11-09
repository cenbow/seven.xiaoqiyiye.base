package seven.ebatong.response;

public class YbaSingleOrderQueryResponse extends AbstractYbaResponse {

	private String status;	//SUCCESS、NOT_FOUND、ERROR
	
	private String outTradeNo;
	
	private String subject;
	
	private String tradeNo;
	
	private String tradeStatus;	//TRADE_FINISHED(付款成功)、TRADE_WAIT_PAY (待付款)
	
	private Double totalFee;
	
	private String sign;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
