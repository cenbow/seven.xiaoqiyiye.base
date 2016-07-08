package seven.xiaoqiyiye.base.common.queue;

@SuppressWarnings("serial")
public class TaskProcessorException extends Exception {

	public TaskProcessorException(ValueEvent event){
		super("++++ TaskProcessor is Exception, the VauleEvent is " + event.getClass().getSimpleName());
	}
	
	public TaskProcessorException(ValueEvent event, Throwable t){
		super("++++ TaskProcessor is Exception, the VauleEvent is " + event.getClass().getSimpleName(), t);
	}
	
	public TaskProcessorException(ValueEvent event, String msg){
		super("++++ TaskProcessor is Exception, the VauleEvent is " + event.getClass().getSimpleName() + ":" + msg);
	}
}
