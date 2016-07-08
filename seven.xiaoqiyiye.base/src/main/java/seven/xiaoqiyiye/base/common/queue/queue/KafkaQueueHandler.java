package seven.xiaoqiyiye.base.common.queue.queue;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import org.springframework.context.ApplicationContext;
import org.springframework.util.SerializationUtils;

import seven.xiaoqiyiye.base.common.queue.AbstractQueueHandler;
import seven.xiaoqiyiye.base.common.queue.QueueHandlers;
import seven.xiaoqiyiye.base.common.queue.ValueEvent;

public class KafkaQueueHandler extends AbstractQueueHandler{

	private static final String DEFAULT_TOPIC = "topic1";
	
	private String topic = DEFAULT_TOPIC;
	
	//生产者配置文件
	private Properties producerProperties = new Properties();
	
	//消费者配置文件
	private Properties consumerProperties = new Properties();
	
	//生产者
	private Producer<String, ValueEvent> producer;
	
	//消费者
	private ConsumerConnector consumer;
	
	@Override
	public Future<ValueEvent> submit(ValueEvent event) {
		put0(event);
		return null;
	}

	@Override
	public List<Future<ValueEvent>> submit(Collection<? extends ValueEvent> events) {
		for(ValueEvent event: events){
			put0(event);
		}
		return null;
	}

	/**
	 * 生产者生产消息
	 * @param event
	 */
	@Override
	public void put0(ValueEvent event) {
		producer.send(new KeyedMessage<String, ValueEvent>(topic, event));
	}

	@Override
	protected boolean isEmpty() {
		return false;
	}

	/**
	 * 消费者调用消息
	 * @throws Exception
	 */
	@Override
	protected void doRun() throws Exception {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<String, ValueEvent>>> consumerMap = consumer.createMessageStreams(topicCountMap, new StringDecoder(new VerifiableProperties()), new ValueEventDecoder(new VerifiableProperties()));
        KafkaStream<String, ValueEvent> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<String, ValueEvent> it = stream.iterator();
        while (it.hasNext()) {
        	ValueEvent event = it.next().message();
            scheduledTask(new Tasker(event));
        }
	}

	@Override
	protected void postApplicationContext(ApplicationContext context) {
		QueueHandlers.initPostAppliactionContext(context);
		initProducerAndConsumer();
	}

	/**
	 * 初始化生产者和消费者
	 */
	private void initProducerAndConsumer(){
		producer = new Producer<String, ValueEvent>(new ProducerConfig(producerProperties));
		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerProperties));
	}

	/**
	 * 设置主题
	 * @param topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * 设置服务端配置文件
	 * @param producerProperties
	 * @throws IOException
	 */
	public void setProducerProperties(Properties producerProperties) throws IOException {
		this.producerProperties = producerProperties;
		this.producerProperties.put("serializer.class", "com.qdlc.p2p.common.core.queue.queue.KafkaQueueHandler$ValueEventEncoder");
	}

	/**
	 * 设置消费端配置文件
	 * @param consumerProperties
	 * @throws IOException
	 */
	public void setConsumerProperties(Properties consumerProperties) throws IOException {
		this.consumerProperties = consumerProperties;
	}
	
	/**
	 * 编码器
	 */
	public static class ValueEventEncoder implements kafka.serializer.Encoder<ValueEvent> {
		
		public ValueEventEncoder(){}
		
		public ValueEventEncoder(VerifiableProperties props) {}
		 
	    @Override
	    public byte[] toBytes(ValueEvent valueEvent) {
	        return SerializationUtils.serialize(valueEvent);
	    }
	}
	
	/**
	 * 解码器
	 */
	public static class ValueEventDecoder implements kafka.serializer.Decoder<ValueEvent> {
		
		public ValueEventDecoder(){}
		
		public ValueEventDecoder(VerifiableProperties props){}
		
	    @Override
	    public ValueEvent fromBytes(byte[] bytes) {
	        return (ValueEvent) SerializationUtils.deserialize(bytes);
	    }
	}
	
	private class Tasker extends NamedEventTasker{
		
		Tasker(ValueEvent valueEvent){
			super(valueEvent);
		}

		@Override
		protected void doRun() throws Exception {
			process(valueEvent);
		}
	}
}
