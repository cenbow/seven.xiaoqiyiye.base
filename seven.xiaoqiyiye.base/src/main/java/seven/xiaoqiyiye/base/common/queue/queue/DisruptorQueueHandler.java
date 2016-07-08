package seven.xiaoqiyiye.base.common.queue.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import seven.xiaoqiyiye.base.common.queue.AbstractQueueHandler;
import seven.xiaoqiyiye.base.common.queue.QueueHandlers;
import seven.xiaoqiyiye.base.common.queue.ValueEvent;
import seven.xiaoqiyiye.base.common.queue.event.AbstractValueEvent;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.WorkHandler;

/**
 * 使用Disruptor框架实现
 *
 * @author linya 2015-12-31
 */
public class DisruptorQueueHandler extends AbstractQueueHandler implements InitializingBean {

    private static final Logger logger = Logger.getLogger(DisruptorQueueHandler.class);

    //RingBuffer容量
    private static final int BUFFER_SIZE = 1024;

    //事件工厂，生成空的EmptyVauleEvent实例
    public static final EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
        public ValueEvent newInstance() {
            return new EmptyValueEvent();
        }
    };

    //创建环形缓存器
    private final RingBuffer<ValueEvent> ringBuffer = new RingBuffer<ValueEvent>(EVENT_FACTORY,
            new SingleThreadedClaimStrategy(BUFFER_SIZE), new BlockingWaitStrategy());

    //序列屏障
    private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

    //事件批量处理器
    private final BatchEventProcessor<ValueEvent> batchEventProcessor = new BatchEventProcessor<ValueEvent>(ringBuffer,
            sequenceBarrier, new TaskEventHandler());

    //保存线程所执行的ValueEvent事件
    private final ThreadLocal<ValueEvent> threadLocal = new ThreadLocal<ValueEvent>();


    @Override
    public void afterPropertiesSet() throws Exception {
        ringBuffer.setGatingSequences(batchEventProcessor.getSequence());
    }

    @Override
    public Future<ValueEvent> submit(ValueEvent event) {
        put(event);
        //事件批量回调器
        return scheduledTask(new BatchEventCallable(batchEventProcessor, event));
    }

    @Override
    public List<Future<ValueEvent>> submit(Collection<? extends ValueEvent> events) {
        List<Future<ValueEvent>> futureList = new ArrayList<Future<ValueEvent>>();
        for (ValueEvent event : events) {
            futureList.add(submit(event));
        }
        return futureList;
    }

    @SuppressWarnings("unused")
    @Override
    public void put0(ValueEvent event) {
        long sequence = ringBuffer.next();
        try {
            if (event != null) {
                boolean capacity = ringBuffer.hasAvailableCapacity(BUFFER_SIZE / 10);
                if (capacity) {

                } else {
                    //将事件对象进行更换（偷天换日），因为ringBuffer中存放的是EmptyValueEvent实例，
                    //需要更换为入队的ValueEvent（只是更改了引用）
                    ValueEvent e = ringBuffer.get(sequence);
                    e = event;
                }
            }
        } catch (Exception e) {
           logger.error(e);
        } finally {
            ringBuffer.publish(sequence);
        }
    }


    @Override
    protected void postApplicationContext(ApplicationContext context) {
        QueueHandlers.initPostAppliactionContext(context);
    }

    @Override
    protected boolean isEmpty() {
        return ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }

    @Override
    protected void doRun() {
        taskExecutor.submit(batchEventProcessor);
    }


    /**
     * 空的ValueEvent事件，由于RingBuffer中预先指定事件对象
     *
     * @author linya
     */
    @SuppressWarnings("serial")
    public static class EmptyValueEvent extends AbstractValueEvent {

    }

    public class TaskEventHandler implements EventHandler<ValueEvent>, WorkHandler<ValueEvent> {
        
    	@Override
        public void onEvent(ValueEvent event, long sequence, boolean endOfBatch)
                throws Exception {
            this.onEvent(event);
        }

        @Override
        public void onEvent(ValueEvent event) throws Exception {
            process(event);
            threadLocal.set(event);
        }
    }

    public class BatchEventCallable extends NamedEventTasker {

        BatchEventProcessor<ValueEvent> processor;

        BatchEventCallable(BatchEventProcessor<ValueEvent> processor, ValueEvent valueEvent) {
            super(valueEvent);
            this.processor = processor;
        }

        @Override
        protected void doRun() throws Exception {
            processor.run();
        }
    }

}
