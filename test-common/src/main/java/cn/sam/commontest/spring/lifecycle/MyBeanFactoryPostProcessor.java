package cn.sam.commontest.spring.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	public MyBeanFactoryPostProcessor() {
        System.out.println("create BeanFactoryPostProcessor");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("call BeanFactoryPostProcessor interface postProcessBeanFactory method");
		String[] names = beanFactory.getBeanDefinitionNames();
		for (String name : names) {
			System.out.println("    definition bean name:" + name);
		}
		System.out.println("  end BeanFactoryPostProcessor interface postProcessBeanFactory method");
	}

}