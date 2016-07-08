package seven.xiaoqiyiye.base.common.queue;

import java.io.Serializable;

/**
 * ValueEvent源自于Disruptor中的概念，表示入队事件。
 * 该接口用于定义业务模型事件，该对象会放入到队列中去，由相对应的任务执行。
 * <p>
 * VauleEvent需要设置一个@Task来找到对应的ITaskProcessor子类对象，这样VauleEvent都可以找到一个与之对应的执行任务。
 * </p>
 * <p/>
 * <p>
 * resultFlag是以前代码遗留下来的，用于标志事件执行完的结果，最后会放入到Global#RESULT_MAP中去。（后续版本中可以考虑去掉）
 * </p>
 *
 * @author linya 2015-12-31
 * @see Task
 * @see ITaskProcessor
 */
public interface ValueEvent extends Serializable {

    /**
     * 设置处理器类型
     * @param taskClass
     */
    void setTaskProcessorClass(Class<? extends ITaskProcessor> taskClass);

    /**
     * 获取处理器类型
     * @return
     */
    Class<? extends ITaskProcessor> getTaskProcessorClass();
    
}
