package seven.ebatong.response;

import seven.ebatong.ParamName;
import seven.ebatong.consts.PaymentType;
import seven.ebatong.response.AbstractYbaResponse;

public class YbaNotifyUrlResponse extends AbstractYbaResponse {

	@ParamName("trade_status")
	private String tradeStatus;
	
	@ParamName("out_trade_no")
	private String outTradeNo;
	
	@ParamName("notify_id")
	private String notifyId;
	
	@ParamName("total_fee")
	private Double totalFee;
	
	private String subject;
	
	@ParamName("payment_type")
	private PaymentType paymentType;
	
	@ParamName("buyer_id")
	private String buyerId;
	
	@ParamName("notify_time")
	private String notifyTime;
	
	@ParamName("notify_type")
	private String notifyType;
	
	@ParamName("trade_no")
	private String tradeNo;

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(500);
		buffer.append("out_trade_no=" + outTradeNo);
		buffer.append(",trade_no=" + tradeNo);
		buffer.append(",notify_time=" + subject);
		buffer.append(",trade_status=" + tradeStatus);
		buffer.append(",buyer_id=" + totalFee);
		buffer.append(",total_fee=" + totalFee);
		buffer.append(",notify_id=" + notifyId);
		buffer.append(",notify_type=" + paymentType);
		buffer.append(",payment_type=" + paymentType);
		buffer.append(",subject=" + subject);
		return buffer.toString();
	}
	
}
