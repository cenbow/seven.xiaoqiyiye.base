package seven.ebatong.request;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import seven.ebatong.ParamName;
import seven.ebatong.YbaConfigs;
import seven.ebatong.YbaRequest;

public abstract class AbstractYbaRequest implements YbaRequest{

	protected String service;
	
	protected String partner;
	
	@ParamName("sign_type")
	protected String signType;
	
	public AbstractYbaRequest() {
		setService();
		initParam();
	}
	
	protected void initParam(){
		this.signType = YbaConfigs.SIGN_TYPE;
		this.partner = YbaConfigs.PARTNER_ID;
	}
	
	protected abstract void setService();
	
	public Map<String, Object> paramMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		for(Class<?> clazz = this.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()){
			Field[] allFields = clazz.getDeclaredFields();
			try {
				for(Field f: allFields){
					String name = f.getName();
					ParamName paramName = f.getAnnotation(ParamName.class);
					String param;
					if(paramName != null){
						param = paramName.value();
					}else{
						param = name;
					}
					PropertyDescriptor pd = new PropertyDescriptor(name, clazz);
					Method method = pd.getReadMethod();
					Object val = method.invoke(this);
					map.put(param, val);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IntrospectionException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public String getService() {
		return service;
	}

	public void setService(String service){
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	
}
