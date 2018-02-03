package cn.sam.test.springcloud.client.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sam.test.springcloud.client.dto.Response;
import cn.sam.test.springcloud.client.dto.User;

@Service
@RequestMapping("example")
public class Example {

    @RequestMapping("test")
    public String test() {
        return new String();
    }

    @RequestMapping("testString")
    public String testString(String name) {
        return new String();
    }

    @RequestMapping("testLong")
    public String testLong(Long name) {
        return new String();
    }
    
    @RequestMapping("testObject")
    public Response testObject(User user) {
        return new Response();
    }
    
}
