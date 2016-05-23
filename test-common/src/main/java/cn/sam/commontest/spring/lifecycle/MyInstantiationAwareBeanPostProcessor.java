package cn.sam.commontest.spring.lifecycle;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

public class MyInstantiationAwareBeanPostProcessor extends
        InstantiationAwareBeanPostProcessorAdapter {

    public MyInstantiationAwareBeanPostProcessor() {
        super();
        System.out.println("create InstantiationAwareBeanPostProcessorAdapter");
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("call InstantiationAwareBeanPostProcessorAdapter class postProcessBeforeInstantiation method; :" +
        		beanName);
        return null;
    }

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("call InstantiationAwareBeanPostProcessorAdapter class postProcessAfterInstantiation method; :" +
        		beanName);
		return true;
	}

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs,
            PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        System.out.println("call InstantiationAwareBeanPostProcessorAdapter class postProcessPropertyValues method; :" +
        		beanName);
        System.out.println("    pvs : " + pvs);
        System.out.println("    pds : " + Arrays.toString(pds));
        System.out.println("    bean : " + bean);
        System.out.println("    beanName : " + beanName);
        System.out.println("  end InstantiationAwareBeanPostProcessorAdapter class postProcessPropertyValues method");
        return pvs;
    }

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("call InstantiationAwareBeanPostProcessorAdapter class postProcessBeforeInitialization method; :" +
        		beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("call InstantiationAwareBeanPostProcessorAdapter class postProcessAfterInitialization method; :" +
        		beanName);
		return bean;
	}
}