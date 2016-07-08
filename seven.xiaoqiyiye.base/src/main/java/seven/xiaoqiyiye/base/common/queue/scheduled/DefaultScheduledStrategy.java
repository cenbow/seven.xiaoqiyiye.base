package seven.xiaoqiyiye.base.common.queue.scheduled;

import java.util.concurrent.TimeUnit;

import seven.xiaoqiyiye.base.common.queue.ReplayValueEvent;

public class DefaultScheduledStrategy implements ScheduledStrategy {

	@Override
	public long calculateExecuteTime(ReplayValueEvent event, ScheduledProcessorMetaData scheduledMetaData) {
		
		long delay = scheduledMetaData.getDelay();
		if(delay < 0){
			return delay;
		}
		
		TimeUnit timeunit = scheduledMetaData.getTimeunit();
		
		//时间最后一次失败时间(如果没有失败时间则为-1)
		long lastestTime = event.lastestFailureTime();
		
		//延时时间
		long delayTime = TimeUnit.MILLISECONDS.convert(delay, timeunit);
		
		//步进时间
		long stepTime = TimeUnit.MILLISECONDS.convert(event.getFailureCount() * scheduledMetaData.getStep(), timeunit);
		
		//下次执行时间间隔
		long intervalTime = lastestTime + delayTime + stepTime - System.currentTimeMillis();
		
		return intervalTime;
	}

}
