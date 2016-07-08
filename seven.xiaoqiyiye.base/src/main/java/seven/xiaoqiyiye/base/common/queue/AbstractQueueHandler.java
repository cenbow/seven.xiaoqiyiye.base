package seven.xiaoqiyiye.base.common.queue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import seven.xiaoqiyiye.base.common.queue.annotation.ScheduledProcessor;
import seven.xiaoqiyiye.base.common.queue.scheduled.ScheduledProcessorMetaData;
import seven.xiaoqiyiye.base.common.queue.scheduled.ScheduledStrategy;
import seven.xiaoqiyiye.base.common.queue.scheduled.ScheduledStrategyFactory;


/**
 * 队列任务处理器
 * @author linya 2015-12-30
 */
public abstract class AbstractQueueHandler implements QueueHandler, ApplicationContextAware{

	private static final Logger log = Logger.getLogger(AbstractQueueHandler.class);
	
	// 从Spring容器中获取所有的ITaskProcessor实现类型
	protected final Map<String, ITaskProcessor> eventTaskProcessorMap = new HashMap<String, ITaskProcessor>();
	
	// 执行线程空闲时间，如果超过空闲时间，执行线程将被关闭。
	// 但是一旦有ValueEvent入队列，执行线程又会启动。
	private static final int THREAD_RUNNING_IDLE_TIME = 5;
	
	// 定义执行线程池大小，项目中执行的大多数为IO型任务，线程池数量可以设置大些。可以在Spring配置文件中配置。
	protected ExecutorService taskExecutor = Executors.newFixedThreadPool(12);
	
	// 工作线程的运行状态值（停止、运行）
	private final AtomicBoolean workThreadStatus = new AtomicBoolean(false);
	
	//延时队列处理延时调度的事件
	private DelayQueue<DelayedEvent> delayQueue = new DelayQueue<DelayedEvent>();
	
	@Override
	public void put(ValueEvent event) {
		
		//如果执行线程处理停止状态，则开启执行线程，并标记状态为运行。
		if(workThreadStatus.compareAndSet(false, true)){
			taskExecutor.execute(new Worker());
		}
		
		//将event放入到队列中去，由子类实现。
		put0(event);
		
	}

	/**
	 * 批量放入到队列中去
	 * @param events
	 */
	@Override
	public void put(Collection<? extends ValueEvent> events) {
		
		if(events == null){
			return;
		}
		
		//如果执行线程处理停止状态，则开启执行线程，并标记状态为运行。
		if(workThreadStatus.compareAndSet(false, true)){
			taskExecutor.execute(new Worker());
		}
		
		for(ValueEvent event: events){
			put0(event);
		}
	}
	
	/**
	 * 子类实现，将ValueEvent事件放入到队列中。
	 * @param event
	 */
	public abstract void put0(ValueEvent event);

	
	/**
	 * 初始化ITaskProcessor处理器接口
	 * @param context
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		
		//获取所有ITaskProcessor接口实现类
		Map<String, ITaskProcessor> processorMap = context.getBeansOfType(ITaskProcessor.class);
		
		//获取TaskEnum和ITaskProcessor的映射关系，
		for(Map.Entry<String, ITaskProcessor> entry: processorMap.entrySet()){
			ITaskProcessor processor = entry.getValue();
			String beanName = StringUtils.capitalize(entry.getKey());
			eventTaskProcessorMap.put(beanName, processor);
		}
		
		if(log.isDebugEnabled()){
			log.info("++++ All ITaskProcessor: " + eventTaskProcessorMap);
		}
		
		//启动延时队列线程
		taskExecutor.submit(new DelayedTasker());
		
		//后置处理(钩子方法)
		postApplicationContext(context);
		
	}
	
	/**
	 * Spring初始化完成之后的后置处理（钩子方法）
	 * @param context
	 */
	protected void postApplicationContext(ApplicationContext context){
		
	}
	
	/**
	 * 处理ValueEvent具体的任务ITaskProcessor
	 * @param event
	 * @throws TaskProcessorException
	 */
	protected void process(ValueEvent event) throws TaskProcessorException{
		
		// 获取执行事件的处理器类型
		Class<? extends ITaskProcessor> taskProcessorClass = event.getTaskProcessorClass();
		
		// 事件名称
		String eventClassName = event.getClass().getName(); 
		
		// 判断是否设置处理器类型
		if(taskProcessorClass == null){
			throw new TaskProcessorException(event, new Throwable("++++ the event is not set taskProcessorClass:" + eventClassName));
		}
		
		// 获取处理器Bean对象
		String beanName = taskProcessorClass.getSimpleName();
		ITaskProcessor processor = eventTaskProcessorMap.get(beanName);
		if(processor == null){
			throw new TaskProcessorException(event, new Throwable("++++ no processor is supports event:" + eventClassName));
		}
		
		//判断处理器是否支持该事件
		if(!processor.supports(event)){
			if(log.isDebugEnabled()){
				log.info("++++ " + taskProcessorClass.getName() + "is not supports event " + eventClassName);
			}
			return;
		}

		try {
			// 判断处理器是否进行Spring事务处理
			boolean transaction = processor.hasTransaction();
			if(transaction){
				processor.transactionProcesss(event);
			}else{
				processor.process(event);
			}
		} catch (Exception e) {
			//如果发生异常，则执行异常处理
			log.info("++++ " + eventClassName + " process exception and replay in queue.", e);
			processor.exceptionProcess(event, e);
		}
	}

	/**
	 * 判断队列是否为空
	 * @return
	 */
	protected abstract boolean isEmpty();
	
	/**
	 * 执行线程如何运行，由子类实现
	 */
	protected abstract void doRun() throws Exception;
	
	/**
	 * 任务定时调度
	 * @param eventTasker
	 */
	protected Future<ValueEvent> scheduledTask(NamedEventTasker eventTasker){
		
		Future<ValueEvent> future = null;
		
		// 获取ValueEvent事件对象
		ValueEvent event = eventTasker.getValueEvent();
		
		// 如果不是ReplayValueEvent类型，则按一般调用处理
		if(!(event instanceof ReplayValueEvent)){
			future = taskExecutor.submit(eventTasker);
			return future;
		}

		// 获取ScheduledProcessor元数据信息
		Class<? extends ITaskProcessor> taskProcessorClass = event.getTaskProcessorClass();
		ScheduledProcessor scheduledAnnotation = taskProcessorClass.getAnnotation(ScheduledProcessor.class);
		ScheduledProcessorMetaData metaData = new ScheduledProcessorMetaData(scheduledAnnotation);
		
		// 任务定时调度策略类，获取剩余调度时间，如果为负数，则调用任务。否则放入到延迟队列中。
		ScheduledStrategy strategy = ScheduledStrategyFactory.getStepStrategy(metaData.getStrategyClass());
		long intervalTime = strategy.calculateExecuteTime((ReplayValueEvent)event, metaData);
		if(intervalTime < 0){
			future = taskExecutor.submit(eventTasker);
		}else{
			delayQueue.put(new DelayedEvent(event, intervalTime));
		}
		
		return future;
	}
	
	
	public void setTaskExecutor(ExecutorService taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	@Override
	public ExecutorService getTaskExecutor() {
		return taskExecutor;
	}

	/**
	 * 定义事件处理任务，子类实现各自的任务
	 */
	protected abstract class NamedEventTasker implements Callable<ValueEvent>{
		
		protected ValueEvent valueEvent;

		protected String newName;
		
		public NamedEventTasker(ValueEvent valueEvent){
			this.valueEvent = valueEvent;
			this.newName = valueEvent.getClass().getSimpleName();
		}
		
		ValueEvent getValueEvent(){
			return valueEvent;
		}

		@Override
		public ValueEvent call() throws Exception{
	        Thread currentThread = Thread.currentThread();
	        String oldName = currentThread.getName();
	        if (newName != null) {
	            setName(currentThread, newName);
	        }
	        try {
	            doRun();
	        } finally {
	            setName(currentThread, oldName);
	        }
	        return valueEvent;
		}
		
		protected abstract void doRun() throws Exception;
		
	    private void setName(Thread thread, String name) {
	        try {
	            thread.setName(name);
	        } catch (SecurityException se) {
	            if (log.isDebugEnabled()) {
	                log.warn("Failed to set the thread name.", se);
	            }
	        }
	    }
	}
	
	/**
	 * 工作线程：负责从QueueHelper中取出数据，并添加到线程池中
	 * @author linya 2015-12-28
	 *
	 */
	private class Worker implements Runnable{
		
		long lastRunningTime = System.currentTimeMillis();
		
		boolean running = true;
		
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			
			if(log.isDebugEnabled()){
				log.info("++++ QueueHandler Worker Thread is Beginning.");
			}
			
			while(running){
				try {
					
					// 如果有队列数据，则执行具体任务
					if(!isEmpty()){
						lastRunningTime = System.currentTimeMillis();
						doRun();
						continue;
					}
					
					// 检测执行线程是否超出空闲，如果超出，则退出执行线程。
					if(canStop()){
						workThreadStatus.compareAndSet(true, false);
						running = false;
					}
					
					// 队列为空时，防止线程空跑，耗用CPU！
					Thread.currentThread().sleep(1000);
					
				} catch (Exception e) {
					log.info(e);
				}
			}
		}
		
		/**
		 * 判断执行线程是否超出空闲时间
		 * @return
		 */
		private boolean canStop(){
			long idleTime = TimeUnit.MILLISECONDS.convert(THREAD_RUNNING_IDLE_TIME, TimeUnit.MINUTES);
			return System.currentTimeMillis() - lastRunningTime > idleTime;
		}
		
	}
	
	/**
	 * 延时队列线程执行任务： 从延迟队列中获取事件，然后存放到线程池中去
	 */
	private class DelayedTasker implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					DelayedEvent delayedEvent = delayQueue.take();
					put(delayedEvent.getEvent());
				} catch (InterruptedException e) {
					log.info(e);
				}
			}
		}
	}
	
	/**
	 * 延时队列事件
	 */
	private static class DelayedEvent implements Delayed{

		//事件对象
	    private ValueEvent event;
	    
	    //执行时间间隔(单位:毫秒)
	    private long intervalTime;
	    
	    //出延时队列时间
	    private long removeTime;
	    
	    public DelayedEvent(ValueEvent event,long intervalTime){
	        this.event = event;
	        this.intervalTime = intervalTime;
	        this.removeTime = TimeUnit.NANOSECONDS.convert(intervalTime, TimeUnit.MILLISECONDS) + System.nanoTime();
	    }
	    
	    public ValueEvent getEvent(){
	    	return event;
	    }
	    
	    @Override
	    public int compareTo(Delayed o) {
	        if (o == null) {
				return 1;
			}
	        if (o == this) {
				return  0;
			}
	        if (o instanceof DelayedEvent){
	        	DelayedEvent tmpDelayedItem = (DelayedEvent)o;
	            if (intervalTime > tmpDelayedItem.intervalTime ) {
	                return 1;
	            }else if (intervalTime == tmpDelayedItem.intervalTime) {
	                return 0;
	            }else {
	                return -1;
	            }
	        }
	        long diff = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
	        return diff > 0 ? 1:diff == 0? 0:-1;
	    }

	    @Override
	    public long getDelay(TimeUnit unit) {
	        return unit.convert(removeTime - System.nanoTime(), unit);
	    }

	    @Override
	    public int hashCode(){
	        return event.hashCode();
	    }
	    
	    @Override
	    public boolean equals(Object object){
	        if (object instanceof DelayedEvent) {
	            return object.hashCode() == hashCode() ?true:false;
	        }
	        return false;
	    }
	    
	}
}
