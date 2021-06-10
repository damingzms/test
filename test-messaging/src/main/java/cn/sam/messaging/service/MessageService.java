package cn.sam.messaging.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import cn.sam.messaging.config.MessageConstants;

@Service
public class MessageService {
    //日期格式化
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RabbitTemplate rabbitTemplate;

    public String sendMsg(String msg) throws Exception {
        try {
            Map<String, Object> map = getMessage(msg);
            rabbitTemplate.convertAndSend("RABBITMQ_DEMO_DIRECT_EXCHANGE", "RABBITMQ_DEMO_DIRECT_ROUTING", map);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    public String sendMsgByFanoutExchange(String msg) throws Exception {
        Map<String, Object> message = getMessage(msg);
        try {
            rabbitTemplate.convertAndSend(MessageConstants.FANOUT_EXCHANGE_DEMO_NAME, "", message);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
	public String sendMsgByTopicExchange(String msg, String routingKey) throws Exception {
		Map<String, Object> message = getMessage(msg);
		try {
			rabbitTemplate.convertAndSend(MessageConstants.TOPIC_EXCHANGE_DEMO_NAME, routingKey, message);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
    public String sendMsgByHeadersExchange(String msg, Map<String, Object> map) throws Exception {
        try {
            MessageProperties messageProperties = new MessageProperties();
            //消息持久化
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            messageProperties.setContentType("UTF-8");
            //添加消息
            messageProperties.getHeaders().putAll(map);
            Message message = new Message(msg.getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(MessageConstants.HEADERS_EXCHANGE_DEMO_NAME, null, message);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    //组装消息体
    private Map<String, Object> getMessage(String msg) {
        String msgId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sendTime = sdf.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);
        map.put("sendTime", sendTime);
        map.put("msg", msg);
        return map;
    }
}