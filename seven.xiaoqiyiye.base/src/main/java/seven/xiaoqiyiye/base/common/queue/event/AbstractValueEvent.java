package seven.xiaoqiyiye.base.common.queue.event;

import seven.xiaoqiyiye.base.common.queue.ITaskProcessor;
import seven.xiaoqiyiye.base.common.queue.ValueEvent;
import seven.xiaoqiyiye.base.common.queue.annotation.Task;

@SuppressWarnings("serial")
public abstract class AbstractValueEvent implements ValueEvent {

	private Class<? extends ITaskProcessor> taskProcessorClass;

    private String resultFlag;

    private String clientIp;

    @Override
    public void setTaskProcessorClass(Class<? extends ITaskProcessor> taskClass) {
        this.taskProcessorClass = taskClass;
    }

    @Override
    public Class<? extends ITaskProcessor> getTaskProcessorClass() {
        if (taskProcessorClass != null) {
            return taskProcessorClass;
        }
        Task task = this.getClass().getAnnotation(Task.class);
        return task == null ? null : task.value();
    }

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
