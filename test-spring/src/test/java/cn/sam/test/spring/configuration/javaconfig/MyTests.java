package cn.sam.test.spring.configuration.javaconfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BeanConfig.class, ComponentScanBeanConfig.class})
public class MyTests {

    @Test
    public void test() {

    }
}
