package seven.xiaoqiyiye.base.common.queue;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * QueueHandler接口，定义处理VauleEvent入队和执行。
 * 可以将VauleEvent放入到队列中等待执行，也可以立即提交到线程中执行，可以返回Future结果。
 * 
 * @author linya 2015-12-31
 * @see QueueHandlers
 */
public interface QueueHandler {

	/**
	 * 将ValueEvent事件放入到队列中去
	 * @param event
	 */
	void put(ValueEvent event);
	
	/**
	 * 将ValueEvent事件放入到队列中去
	 * @param event
	 */
	void put(Collection<? extends ValueEvent> events);
	
	/**
	 * 将ValueEvent事件提交到线程池中去，并返回一个Future
	 * @param event
	 * @return
	 */
	Future<ValueEvent> submit(ValueEvent event);
	
	/**
	 * 批量提交
	 * @param event
	 * @return
	 */
	List<Future<ValueEvent>> submit(Collection<? extends ValueEvent> events);
	
	/**
	 * 获取执行的线程池
	 * @return
	 */
	ExecutorService getTaskExecutor();
	
}
