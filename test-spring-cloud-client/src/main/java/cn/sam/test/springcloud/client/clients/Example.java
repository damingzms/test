package cn.sam.test.springcloud.client.clients;

import org.springframework.stereotype.Service;

import cn.sam.test.springcloud.client.dto.Response;
import cn.sam.test.springcloud.client.dto.User;

@Service
public class Example {

    public String test() {
        return new String();
    }
    
    public String testString(String name) {
        return new String();
    }
    
    public String testLong(Long name) {
        return new String();
    }
    
    public Response testObject(User user) {
        return new Response();
    }
    
}
