package seven.xiaoqiyiye.base.common.highcharts.demo;

public enum TYPE {
	ONE("1", "类型一"),
	TWO("2", "类型二"),
	THREE("3", "类型三");
	
	String name;
	String type;
	
	TYPE(String type, String name){
		this.name = name;
		this.type = type;
	}
	
	static String getNameByType(String type){
		for(TYPE em: TYPE.values()){
			if(em.type.equals(type)){
				return em.name;
			}
		}
		return "";
	}
}
