package cn.sam.test.spring.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanB implements InitializingBean, DisposableBean,
		BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, ApplicationContextAware {
	
	private String name;

	public BeanB() {
		System.out.println("create BeanB");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("call setName method, value : " + name);
		this.name = name;
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("call BeanNameAware interface, name is:" + name);
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		System.out.println("call BeanClassLoaderAware interface, name is:" + name);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("call BeanFactoryAware interface, name is:" + name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		System.out.println("call ApplicationContextAware interface, name is:" + name);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("call InitializingBean interface, name is:" + name);
	}

	public void _init() {
		System.out.println("call bean init method, name is:" + name);
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("call DisposableBean interface, name is:" + name);
	}

	public void _destory() {
		System.out.println("call bean destory method, name is:" + name);
	}

}