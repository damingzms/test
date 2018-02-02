package cn.sam.test.springboot;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example/")
public class PathExample {

    @RequestMapping("test")
    String test() {
        return "Hello Test!";
    }

    @RequestMapping("testString")
    String testString(@RequestBody String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping("testLong")
    String testLong(@RequestBody Long name) {
        return "Hello " + name + "!";
    }

    @RequestMapping("testObject")
    Object testObject(@RequestBody User user) {
        String result = "Hello " + user.getName() + "!";
        Response response = new Response();
        response.setResult(result);
		return response;
    }
    
    public static class User {
    	
    	private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
    	
    }
    
    public static class Response {
    	
    	private String result;

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
    	
    }

}