package seven.xiaoqiyiye.base.common.queue.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import seven.xiaoqiyiye.base.common.queue.ITaskProcessor;


@Inherited
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {
	
	Class<? extends ITaskProcessor> value();
	
}
