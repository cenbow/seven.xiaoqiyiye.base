package seven.xiaoqiyiye.base.common.queue.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import seven.xiaoqiyiye.base.common.queue.AbstractQueueHandler;
import seven.xiaoqiyiye.base.common.queue.QueueHandlers;
import seven.xiaoqiyiye.base.common.queue.ValueEvent;


/**
 * 使用阻塞队列LinkedBlockingQueue实现队列处理。
 * @author linya 2015-12-31
 */
public class ConcurrentQueueHandler extends AbstractQueueHandler {

	private static final Logger logger = Logger.getLogger(ConcurrentQueueHandler.class);

	private BlockingQueue<ValueEvent> eventQueue = new LinkedBlockingQueue<ValueEvent>();
	
	@Override
	public void put0(ValueEvent event) {
		try {
			eventQueue.put(event);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	@Override
	public Future<ValueEvent> submit(ValueEvent event) {
		return scheduledTask(new Tasker(event));
	}
	
	@Override
	public List<Future<ValueEvent>> submit(Collection<? extends ValueEvent> events) {
		List<Future<ValueEvent>> futureList = new ArrayList<Future<ValueEvent>>();
		for(ValueEvent event: events){
			futureList.add(submit(event));
		}
		return futureList;
	}

	@Override
	protected boolean isEmpty() {
		return eventQueue.isEmpty();
	}

	@Override
	protected void doRun() {
		ValueEvent event = eventQueue.poll();
		if(event != null){
			submit(event);
		}
	}
	
	/**
	 * 执行ITaskProcessor接口
	 * @author linya 2015-12-28
	 */
	private class Tasker extends NamedEventTasker{
		
		Tasker(ValueEvent valueEvent) {
			super(valueEvent);
		}

		@Override
		protected void doRun() throws Exception {
			process(valueEvent);
		}
	}

	@Override
	protected void postApplicationContext(ApplicationContext context) {
		QueueHandlers.initPostAppliactionContext(context);
	}

}
