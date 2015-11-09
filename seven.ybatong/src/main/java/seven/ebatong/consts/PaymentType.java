package seven.ebatong.consts;

public enum PaymentType {
	one("1");
	
	String code;
	
	private PaymentType(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}
	
}
