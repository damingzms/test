package cn.sam.messaging.consumer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import cn.sam.messaging.config.MessageConstants;

@Component
public class MessageConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

	// 使用queuesToDeclare属性，如果不存在则会创建队列，2.0版本以后才提供
//	@RabbitListener(queuesToDeclare = @Queue("RABBITMQ_DEMO_TOPIC"))
	@RabbitListener(queues = "RABBITMQ_DEMO_TOPIC")
	public void process(Map<String, Object> msg) {
		LOG.info("接收消息：{}", msg.toString());
	}
	
	
	

	@RabbitListener(queues = MessageConstants.FANOUT_EXCHANGE_QUEUE_TOPIC_A)
	public void processFanoutA(Map<String, Object> map) {
		System.out.println("队列A收到消息：" + map.toString());
	}

	@RabbitListener(queues = MessageConstants.FANOUT_EXCHANGE_QUEUE_TOPIC_B)
	public void processFanoutB(Map<String, Object> map) {
		System.out.println("队列B收到消息：" + map.toString());
	}

	
	
	
	@RabbitListener(queues = MessageConstants.TOPIC_EXCHANGE_QUEUE_A)
	public void TopicExchangeConsumerA(Map<String, Object> map) {
		System.out.println("队列[" + MessageConstants.TOPIC_EXCHANGE_QUEUE_A + "]收到消息：" + map.toString());
	}

	@RabbitListener(queues = MessageConstants.TOPIC_EXCHANGE_QUEUE_B)
	public void TopicExchangeConsumerB(Map<String, Object> map) {
		System.out.println("队列[" + MessageConstants.TOPIC_EXCHANGE_QUEUE_B + "]收到消息：" + map.toString());
	}

	@RabbitListener(queues = MessageConstants.TOPIC_EXCHANGE_QUEUE_C)
	public void TopicExchangeConsumerC(Map<String, Object> map) {
		System.out.println("队列[" + MessageConstants.TOPIC_EXCHANGE_QUEUE_C + "]收到消息：" + map.toString());
	}
	
	
	

    @RabbitListener(queues = MessageConstants.HEADERS_EXCHANGE_QUEUE_A)
	public void HeadersExchangeConsumerA(Message message) throws Exception {
		MessageProperties messageProperties = message.getMessageProperties();
		String contentType = messageProperties.getContentType();
		System.out.println("队列[" + MessageConstants.HEADERS_EXCHANGE_QUEUE_A + "]收到消息："
				+ new String(message.getBody(), contentType));
	}

	@RabbitListener(queues = MessageConstants.HEADERS_EXCHANGE_QUEUE_B)
	public void HeadersExchangeConsumerB(Message message) throws Exception {
		MessageProperties messageProperties = message.getMessageProperties();
		String contentType = messageProperties.getContentType();
		System.out.println("队列[" + MessageConstants.HEADERS_EXCHANGE_QUEUE_B + "]收到消息："
				+ new String(message.getBody(), contentType));
	}

}