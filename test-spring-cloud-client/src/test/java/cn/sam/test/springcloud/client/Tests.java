package cn.sam.test.springcloud.client;

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

import cn.sam.test.springcloud.client.dto.Response;
import cn.sam.test.springcloud.client.dto.Role;
import cn.sam.test.springcloud.client.dto.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {
	
	@Autowired
	private ExampleServiceFactory factory;

	@Test
	public void test() {
		String test = factory.getExample().test();
		System.out.println(test);
	}

	@Test
	public void testString() {
		String test = factory.getExample().testString("Samuel");
		System.out.println(test);
	}

	@Test
	public void testLong() {
		String test = factory.getExample().testLong(100L);
		System.out.println(test);
	}

	@Test
	public void testObject() {
		User user = new User();
		user.setName("Samuel");
		Response response = factory.getExample().testObject(user);
		System.out.println(response.getResult());
	}

    @Test
    public void testObjectAndString() {
		User user = new User();
		user.setName("Samuel");
		Response response = factory.getExample().testObjectAndString(user, "Jojo");
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
		Response response = factory.getExample().testMultiObject(user, role);
		System.out.println(response.getResult());
    }

    @Test
    public void testCollection() {
    	List<User> users = new ArrayList<>();
		User user = new User();
		user.setName("Samuel");
		user.setBirthday(new Date());
		users.add(user);
		Response response = factory.getExample().testCollection(users);
		System.out.println(response.getResult());
    }

    @Test
    public void testMap() {
    	Map<String, String> map = new HashMap<>();
    	map.put("name", "Samuel");
		Response response = factory.getExample().testMap(map);
		System.out.println(response.getResult());
    }

}