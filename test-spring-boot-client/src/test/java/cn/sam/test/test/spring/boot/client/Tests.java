package cn.sam.test.test.spring.boot.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sam.test.test.spring.boot.client.dto.Response;
import cn.sam.test.test.spring.boot.client.dto.Role;
import cn.sam.test.test.spring.boot.client.dto.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {
	
	@Autowired
	private TestSpringBootServiceFactory factory;

	@Test
	public void test() {
		String test = factory.getPathExampleService().test();
		System.out.println(test);
	}

	@Test
	public void testString() {
		String test = factory.getPathExampleService().testString("Samuel");
		System.out.println(test);
	}

	@Test
	public void testLong() {
		String test = factory.getPathExampleService().testLong(100L);
		System.out.println(test);
	}

	@Test
	public void testObject() {
		User user = new User();
		user.setName("Samuel");
		Response response = factory.getPathExampleService().testObject(user);
		System.out.println(response.getResult());
	}

    @Test
    public void testObjectAndString() {
		User user = new User();
		user.setName("Samuel");
		Response response = factory.getPathExampleService().testObjectAndString(user, "Jojo");
		System.out.println(response.getResult());
    }

    @Test
    public void testMultiObject() {
		User user = new User();
		user.setName("Samuel");
		user.setBirthday(new Date());
		Role role = new Role();
		role.setRoleName("admin");
		role.setCreatedDate(new Date());
		user.setRole(role);
//		role.setUser(user);
		Response response = factory.getPathExampleService().testMultiObject(user, role);
		System.out.println(response.getResult());
    }

    @Test
    public void testCollection() {
    	List<User> users = new ArrayList<>();
		User user = new User();
		user.setName("Samuel");
		user.setBirthday(new Date());
		users.add(user);
		Response response = factory.getPathExampleService().testCollection(users);
		System.out.println(response.getResult());
    }

    @Test
    public void testMap() {
    	Map<String, String> map = new HashMap<>();
    	map.put("name", "Samuel");
		Response response = factory.getPathExampleService().testMap(map);
		System.out.println(response.getResult());
    }

    @Test
    public void testReturnPrimitive() {
		int response = factory.getPathExampleService().testReturnPrimitive();
		System.out.println(response);
    }

}