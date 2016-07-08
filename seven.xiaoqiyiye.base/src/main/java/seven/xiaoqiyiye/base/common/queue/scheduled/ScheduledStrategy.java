package seven.xiaoqiyiye.base.common.queue.scheduled;

import seven.xiaoqiyiye.base.common.queue.ReplayValueEvent;

public interface ScheduledStrategy {

	/**
	 * 计算执行时间
	 * @param event 执行事件
	 * @param step
	 * @param timeUnit 时间单位
	 * @return 距离下一个执行点的时间间隔（毫秒）
	 */
	long calculateExecuteTime(ReplayValueEvent event, ScheduledProcessorMetaData scheduledMetaData);
	
}
