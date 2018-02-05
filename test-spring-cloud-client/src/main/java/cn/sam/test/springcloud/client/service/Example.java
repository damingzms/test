package cn.sam.test.springcloud.client.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sam.test.springcloud.client.dto.Response;
import cn.sam.test.springcloud.client.dto.Role;
import cn.sam.test.springcloud.client.dto.User;

@Service
@RequestMapping("/example/")
public class Example {

    @RequestMapping("test")
    public String test() {
        return new String();
    }

    @RequestMapping("testString")
    public String testString(@RequestParam("name") String name) {
        return new String();
    }

    @RequestMapping("testLong")
    public String testLong(@RequestParam("name") Long name) {
        return new String();
    }
    
    @RequestMapping("testObject")
    public Response testObject(@RequestBody User user) {
        return new Response();
    }

    @RequestMapping("testObjectAndString")
    public Response testObjectAndString(@RequestBody User user, @RequestParam("newName") String newName) {
        return new Response();
    }

    @RequestMapping("testMultiObject")
    public Response testMultiObject(@RequestBody User user, Role role) {
        return new Response();
    }

    @RequestMapping("testCollection")
    public Response testCollection(@RequestBody List<User> users) {
        return new Response();
    }

    @RequestMapping("testMap")
    public Response testMap(@RequestBody Map<String, String> map) {
        return new Response();
    }
    
}
