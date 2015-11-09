package seven.ebatong.request;


import seven.ebatong.ParamName;
import seven.ebatong.YbaConfigs;
import seven.ebatong.consts.BankCode;
import seven.ebatong.consts.PayMethod;
import seven.ebatong.consts.PaymentType;

public class YbaPayRequest extends AbstractYbaRequest {

	@ParamName("input_charset")
	protected String inputCharset;
	
	@ParamName("notify_url")
	private String notifyUrl;
	
	@ParamName("return_url")
	private String returnUrl;
	
	@ParamName("error_notify_url")
	private String errorNotifyUrl;
	
	@ParamName("anti_phishing_key")
	private String antiPhishingKey;
	
	@ParamName("exter_invoke_ip")
	private String invokeIp;
	
	@ParamName("out_trade_no")
	private String outTradeNo;
	
	private String subject;
	
	@ParamName("payment_type")
	private PaymentType paymentType;
	
	@ParamName("seller_email")
	private String sellerEmail;
	
	@ParamName("seller_id")
	private String sellerId;
	
	@ParamName("buyer_email")
	private String buyerEmail;
	
	@ParamName("buyer_id")
	private String buyerId;
	
	private Double price;
	
	@ParamName("total_fee")
	private Double totalFee;
	
	private String quantity;
	
	@ParamName("show_url")
	private String showUrl;
	
	private String body;
	
	@ParamName("pay_method")
	private PayMethod payMethod;
	
	@ParamName("default_bank")
	private BankCode bankCode;
	
	@ParamName("royalty_parameters")
	private String royaltyParameters;
	
	@ParamName("royalty_type")
	private String royaltyType;
	
	public String getRequestUrl() {
		return YbaConfigs.PAY_GATEWAY;
	}

	@Override
	protected void setService() {
		this.service = "create_direct_pay_by_user";
	}
	

	@Override
	protected void initParam() {
		super.initParam();
		this.inputCharset = YbaConfigs.INPUT_CHARSET;
		this.notifyUrl = YbaConfigs.NOTIFY_URL;
		this.returnUrl = YbaConfigs.RETURN_URL;
		this.sellerId = YbaConfigs.PARTNER_ID;
		this.payMethod = PayMethod.bankPay;
		this.paymentType = PaymentType.one;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getErrorNotifyUrl() {
		return errorNotifyUrl;
	}

	public void setErrorNotifyUrl(String errorNotifyUrl) {
		this.errorNotifyUrl = errorNotifyUrl;
	}

	public String getAntiPhishingKey() {
		return antiPhishingKey;
	}

	public void setAntiPhishingKey(String antiPhishingKey) {
		this.antiPhishingKey = antiPhishingKey;
	}

	public String getInvokeIp() {
		return invokeIp;
	}

	public void setInvokeIp(String invokeIp) {
		this.invokeIp = invokeIp;
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

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public PayMethod getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(PayMethod payMethod) {
		this.payMethod = payMethod;
	}

	public BankCode getBankCode() {
		return bankCode;
	}

	public void setBankCode(BankCode bankCode) {
		this.bankCode = bankCode;
	}

	public String getRoyaltyParameters() {
		return royaltyParameters;
	}

	public void setRoyaltyParameters(String royaltyParameters) {
		this.royaltyParameters = royaltyParameters;
	}

	public String getRoyaltyType() {
		return royaltyType;
	}

	public void setRoyaltyType(String royaltyType) {
		this.royaltyType = royaltyType;
	}

	
}
