package seven.ebatong.consts;

public enum CertType {

	c1("01", "身份证"),
	c2("02", "军官证"),
	c3("03", "护照"),
	c4("04", "户口薄"),
	c5("05", "回乡证"),
	c6("06", "其他");
	
	String code;
	String name;
	
	private CertType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static CertType getType(String code){
		for(CertType eu: CertType.values()){
			if(eu.code.equals(code)){
				return eu;
			}
		}
		return c6;
	}
	
	public static String getName(String code){
		return getType(code).name;
	}
}
