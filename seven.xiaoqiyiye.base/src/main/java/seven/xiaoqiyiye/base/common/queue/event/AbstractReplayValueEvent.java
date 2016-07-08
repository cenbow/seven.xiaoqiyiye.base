package seven.xiaoqiyiye.base.common.queue.event;

import java.util.concurrent.atomic.AtomicInteger;

import seven.xiaoqiyiye.base.common.queue.ReplayValueEvent;


/**
 * 定义事件在失败的情况下，可以再重新执行，默认失败后可以重新执行一次
 * @author linya 2016-01-11
 */
public abstract class AbstractReplayValueEvent extends AbstractValueEvent implements ReplayValueEvent {

	private static final long serialVersionUID = 4969358294390867547L;

	private static final int DEFAULT_FAILURE_COUNT = 5;
	
	// 保存失败次数
	protected AtomicInteger count = new AtomicInteger(0);
	
	// 最近一次执行失败时间
	protected long lastestFailureTime = -1;
	
	// 允许执行失败的最大次数（子类可以重新设置次数）
	protected int maxFailureCount;
	
	protected AbstractReplayValueEvent(){
		maxFailureCount = DEFAULT_FAILURE_COUNT;
	}
	
	/**
	 * 允许执行失败的最大次数
	 * @return
	 */
	protected int getMaxFailureCount(){
		return maxFailureCount;
	}
	
	@Override
	public int incrementAndGetFailureCount() {
		return count.incrementAndGet();
	}
	
	@Override
	public int getFailureCount() {
		return count.get();
	}

	@Override
	public boolean isFinalFailure() {
		return count.compareAndSet(getFailureCount(), getMaxFailureCount());
	}

	@Override
	public long lastestFailureTime() {
		return lastestFailureTime;
	}

	@Override
	public void setLastestFailureTime(long failureTime) {
		this.lastestFailureTime = failureTime;
	}
	
}
