package seven.xiaoqiyiye.base.common.queue;

/**
 * 定义可以重复执行的队列事件
 * @author linya 2016-01-11
 */
public interface ReplayValueEvent extends ValueEvent {

	/**
	 * 事件执行失败将会加一
	 * @param count
	 */
	int incrementAndGetFailureCount();
	
	/**
	 * 获取失败次数
	 * @return
	 */
	int getFailureCount();
	
	/**
	 * 判断是否到达最终的失败次数
	 * @return
	 */
	boolean isFinalFailure();
	
	/**
	 * 最近一次执行失败时间
	 * @return
	 */
	long lastestFailureTime();
	
	/**
	 * 设置上次执行失败时间
	 * @param failureTime
	 */
	void setLastestFailureTime(long failureTime);
	
}
