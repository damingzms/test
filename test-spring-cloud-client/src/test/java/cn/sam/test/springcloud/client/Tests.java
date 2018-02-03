package cn.sam.test.springcloud.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.sam.test.springcloud.client.dto.Response;
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
	
}