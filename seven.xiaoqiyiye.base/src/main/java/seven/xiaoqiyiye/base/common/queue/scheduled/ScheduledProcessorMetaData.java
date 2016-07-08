package seven.xiaoqiyiye.base.common.queue.scheduled;

import java.util.concurrent.TimeUnit;

import seven.xiaoqiyiye.base.common.queue.annotation.ScheduledProcessor;

public class ScheduledProcessorMetaData {

	//延时
	private long delay = -1;
	
	//延时步进
	private long step;
	
	//延时策略
	private Class<? extends ScheduledStrategy> strategyClass;
	
	//时间单位
	private TimeUnit timeunit;
	
	public ScheduledProcessorMetaData(ScheduledProcessor scheduledAnnotation){
		if(scheduledAnnotation == null){
			return;
		}
		this.delay = scheduledAnnotation.delay();
		this.step = scheduledAnnotation.step();
		this.strategyClass = scheduledAnnotation.stepStrategy();
		this.timeunit = scheduledAnnotation.timeunit();
	}

	public long getDelay() {
		return delay;
	}

	public long getStep() {
		return step;
	}

	public Class<? extends ScheduledStrategy> getStrategyClass() {
		return strategyClass;
	}

	public TimeUnit getTimeunit() {
		return timeunit;
	}
	
}
