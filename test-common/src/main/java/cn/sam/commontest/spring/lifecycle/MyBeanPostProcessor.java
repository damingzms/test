package cn.sam.commontest.spring.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {

	public MyBeanPostProcessor() {
        System.out.println("create BeanPostProcessor");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("call BeanPostProcessor interface postProcessBeforeInitialization method; :"
						+ beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("call BeanPostProcessor interface postProcessAfterInitialization method; :"
						+ beanName);
		return bean;
	}

}