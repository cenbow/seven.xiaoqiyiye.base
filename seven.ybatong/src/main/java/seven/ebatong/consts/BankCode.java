package seven.ebatong.consts;

public enum BankCode {
	ICBC_B2C("ICBC_B2C", "中国工商银行"),
	ABC_B2C("ABC_B2C", "中国农业银行"),
	CCB_B2C("CCB_B2C", "中国建设银行"),
	CMBCD_B2C("CMBCD_B2C", "民生银行"),
	BOCSH_B2C("BOCSH_B2C", "中国银行"),
	POSTGC_B2C("POSTGC_B2C", "中国邮政储蓄银行"),
	CMB_B2C("CMB_B2C", "招商银行");
	
	String code;
	String name;
	
	private BankCode(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static BankCode getType(String code){
		for(BankCode eu: BankCode.values()){
			if(eu.code.equals(code)){
				return eu;
			}
		}
		return ICBC_B2C;
	}
	
	public static String getName(String code){
		return getType(code).name;
	}

	@Override
	public String toString() {
		return code;
	}
	
	
}
