package cn.sam.test.springboot;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example/")
public class PathExample {

    @RequestMapping("test")
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
    
    public static class User {
    	
    	private String name;
    	
    	private Date birthday;
    	
    	private Role role;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getBirthday() {
			return birthday;
		}

		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}
    	
    }
    
    public static class Role {
    	
    	private String roleName;
    	
    	private Date createdDate;
    	
    	private User user;

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
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