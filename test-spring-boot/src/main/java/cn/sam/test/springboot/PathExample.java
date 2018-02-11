package cn.sam.test.springboot;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.sam.test.springboot.bean.Response;
import cn.sam.test.springboot.bean.Role;
import cn.sam.test.springboot.bean.User;

@RestController
@RequestMapping(value = "/example/", method = RequestMethod.POST)
public class PathExample {

    @RequestMapping(value = "test", method = RequestMethod.GET)
    String test() {
        return "Hello Test!";
    }

    @RequestMapping(value = "testString")
    String testString(@RequestParam("name") String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping("testLong")
    String testLong(@RequestParam("name") Long name) {
        return "Hello " + name + "!";
    }

    @RequestMapping("testObject")
    Response testObject(@RequestBody User user) {
        String result = "Hello " + user.getName() + "!";
        Response response = new Response();
        response.setResult(result);
		return response;
    }

    @RequestMapping("testObjectAndString")
    Response testObjectAndString(@RequestBody User user, @RequestParam("newName") String newName) {
        String result = "Hello " + newName + "!";
        Response response = new Response();
        response.setResult(result);
		return response;
    }

    @RequestMapping("testMultiObject")
    Response testMultiObject(@RequestBody User user, Role role) {
        String result = "Hello " + user.getName() + "! " + role.getRoleName();
        Response response = new Response();
        response.setResult(result);
		return response;
    }

    @RequestMapping("testCollection")
    Response testCollection(@RequestBody List<User> users) {
        String result = "Hello " + users.get(0).getName() + "!";
        Response response = new Response();
        response.setResult(result);
		return response;
    }

    @RequestMapping("testMap")
    Response testMap(@RequestBody Map<String, String> map) {
        String result = "Hello " + map.get("name") + "!";
        Response response = new Response();
        response.setResult(result);
		return response;
    }

    @RequestMapping("testReturnPrimitive")
    int testReturnPrimitive() {
		return 1;
    }

}