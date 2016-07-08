package seven.xiaoqiyiye.base.common.queue.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import seven.xiaoqiyiye.base.common.queue.scheduled.DefaultScheduledStrategy;
import seven.xiaoqiyiye.base.common.queue.scheduled.ScheduledStrategy;

/**
 * 定义TaskProcessor处理的调度时间
 * @author linya
 */
@Inherited
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledProcessor {

	//延时执行时间
	long delay() default -1;
	
	//延时步进值
	long step() default 0;
	
	//调度策略
	Class<? extends ScheduledStrategy> stepStrategy() default DefaultScheduledStrategy.class;
	
	//时间单位，默认为秒
	TimeUnit timeunit() default TimeUnit.SECONDS;
	
}
