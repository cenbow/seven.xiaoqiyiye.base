package seven.xiaoqiyiye.base.common.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;


/**
 * 队列操作的工具类
 * @author linya 2015-12-31
 */
public class QueueHandlers{

	private static QueueHandler handler;
	
	private QueueHandlers(){

	}
	
	public static void initPostAppliactionContext(ApplicationContext context){
		// 获取QueueHandler实现类
		Map<String, QueueHandler> handlerMap = context.getBeansOfType(QueueHandler.class);
		if(handlerMap == null || handlerMap.isEmpty()){
			throw new BeanCreationException("++++ No found a bean of QueueHandler interface.");
		}
		// 如果配置了多个，只需要获取一个
		List<QueueHandler> handlers = new ArrayList<QueueHandler>(handlerMap.values());
		handler = handlers.get(0);
	}
	
	public static void put(ValueEvent event){
		handler.put(event);
	}
	
	public static void put(Collection<? extends ValueEvent> events){
		handler.put(events);
	}
	
	public static Future<ValueEvent> submit(ValueEvent event){
		return handler.submit(event);
	}
	
	public static List<Future<ValueEvent>> submit(Collection<ValueEvent> eventList){
		return handler.submit(eventList);
	}
	
	public static ExecutorService getTaskExecutor(){
		return handler.getTaskExecutor();
	}
	
}
