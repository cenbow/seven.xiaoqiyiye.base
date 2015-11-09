package seven.ebatong.consts;

public enum PayMethod {
	
	directPay,
	bankPay;

	@Override
	public String toString() {
		return name();
	}
	
}