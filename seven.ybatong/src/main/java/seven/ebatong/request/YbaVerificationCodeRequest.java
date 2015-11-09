package seven.ebatong.request;

import seven.ebatong.ParamName;
import seven.ebatong.YbaConfigs;
import seven.ebatong.consts.BankCode;
import seven.ebatong.consts.CertType;

public class YbaVerificationCodeRequest extends AbstractYbaRequest{
	
	@ParamName("customer_id")
	private String customerId;	//客户号
	
	@ParamName("card_no")
	private String cardNo;		//卡号
	
	@ParamName("real_name")
	private String realName;	//真实名
	
	@ParamName("cert_type")
	private CertType certType;	//证件号
	
	@ParamName("out_trade_no")
	private String outTradeNo;	//外部请求编号(必填)
	
	private Double amount;		//金额(单位：元，精度：2位)(必填)
	
	@ParamName("bank_code")
	private BankCode bankCode;
	
	@ParamName("card_bind_mobile_phone_no")
	private String cardBindMobilePhoneNo;	//绑定手机号码
	
	public String getRequestUrl() {
		return YbaConfigs.URL_VERIFICATION_CODE;
	}

	@Override
	protected void setService() {
		this.service = "ebatong_mp_dyncode";
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public CertType getCertType() {
		return certType;
	}

	public void setCertType(CertType certType) {
		this.certType = certType;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public BankCode getBankCode() {
		return bankCode;
	}

	public void setBankCode(BankCode bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardBindMobilePhoneNo() {
		return cardBindMobilePhoneNo;
	}

	public void setCardBindMobilePhoneNo(String cardBindMobilePhoneNo) {
		this.cardBindMobilePhoneNo = cardBindMobilePhoneNo;
	}


	
	
}
