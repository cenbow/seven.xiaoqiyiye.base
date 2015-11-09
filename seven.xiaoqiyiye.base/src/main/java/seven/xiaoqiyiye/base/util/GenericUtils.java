package seven.xiaoqiyiye.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericUtils {

	/**
	 * @description 获取父类的泛型类
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenericType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();// 得到泛型父类
		// 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		// 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class
		Type[] parameters = ((ParameterizedType) genType)
				.getActualTypeArguments();
		if (index > parameters.length || index < 0) {
			throw new RuntimeException("你输入的索引号"
					+ (index < 0 ? "不能小于0" : "超出了参数的总数"));
		}
		if (!(parameters[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) parameters[index];
	}

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenericType(Class clazz){
		return getSuperClassGenericType(clazz, 0);
	}
}
