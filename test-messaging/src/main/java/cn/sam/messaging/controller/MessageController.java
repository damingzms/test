package cn.sam.messaging.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sam.messaging.service.MessageService;

@RestController
@RequestMapping("/mall/rabbitmq")
public class MessageController {
	@Resource
	private MessageService rabbitMQService;

	/**
	 * 发送消息
	 * 
	 * @author java技术爱好者
	 */
	@GetMapping("/sendMsg")
	public String sendMsg(@RequestParam(name = "msg") String msg) throws Exception {
		return rabbitMQService.sendMsg(msg);
	}

	/**
	 * 发布消息
	 *
	 * @author java技术爱好者
	 */
	@GetMapping("/publish")
	public String publish(@RequestParam(name = "msg") String msg) throws Exception {
		return rabbitMQService.sendMsgByFanoutExchange(msg);
	}
	
    /**
     * 通配符交换机发送消息
     *
     * @author java技术爱好者
     */
	@GetMapping("/topicSend")
    public String topicSend(@RequestParam(name = "msg") String msg, @RequestParam(name = "routingKey") String routingKey) throws Exception {
        return rabbitMQService.sendMsgByTopicExchange(msg, routingKey);
    }
	
	@SuppressWarnings("unchecked")
	@GetMapping("/headersSend")
    public String headersSend(@RequestParam(name = "msg") String msg,
                              @RequestParam(name = "json") String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, Map.class);
        return rabbitMQService.sendMsgByHeadersExchange(msg, map);
    }
	
}