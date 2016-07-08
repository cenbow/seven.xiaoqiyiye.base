package seven.xiaoqiyiye.base.common.queue;

import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * ITaskProcessor定义执行的任务Task，负责执行放入到队列中ValueEvent事件。
 * <p>
 * 		ITaskProcessor的实现类都需要 使用{@link @Task}注解
 * </p>
 * 
 * @author linya 2015-12-31
 * @see Task
 * @see ValueEvent
 */
public interface ITaskProcessor {

	/**
	 * 指定可以支持的VauleEvent事件类型，否则将不会执行process(ValueEvent event)方法
	 * @param event
	 * @return
	 */
	boolean supports(ValueEvent event);
	
	/**
	 * 设置是否在事务内执行，返回true则在事务内执行，否则不在事务内执行
	 * 默认返回false
	 * @return
	 */
	boolean hasTransaction();
	
	/**
	 * 执行给定的VauleEvent事件(不在事务内)
	 * @param event
	 * @throws TaskProcessorException
	 */
	void process(ValueEvent event) throws TaskProcessorException;
	
	/**
	 * 执行给定的VauleEvent事件(在事务内处理)
	 * @param event
	 * @throws TaskProcessorException
	 */
	@Transactional(rollbackFor=TaskProcessorException.class)
	void transactionProcesss(ValueEvent event) throws TaskProcessorException;
	
	/**
	 * 异常处理
	 * @param event
	 * @param exception
	 */
	void exceptionProcess(ValueEvent event, Exception exception);
}
