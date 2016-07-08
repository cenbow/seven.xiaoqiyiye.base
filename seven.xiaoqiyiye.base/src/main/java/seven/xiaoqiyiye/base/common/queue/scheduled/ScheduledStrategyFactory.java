package seven.xiaoqiyiye.base.common.queue.scheduled;

/**
 * 获取策略类型
 * @author linya
 */
public class ScheduledStrategyFactory {

	private static final ScheduledStrategy strategy = new DefaultScheduledStrategy();
	
	public static ScheduledStrategy getStepStrategy(Class<? extends ScheduledStrategy> strategyClass){
		return strategy;
	}
	
}
