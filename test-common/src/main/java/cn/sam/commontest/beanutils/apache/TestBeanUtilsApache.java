package cn.sam.commontest.beanutils.apache;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import cn.sam.commontest.bean.Apple;
import cn.sam.commontest.bean.Orange;

public class TestBeanUtilsApache {

	public static void main(String[] args) throws Exception {
		Apple a = new Apple();
		a.setColor("red");
		a.setWeight(10);
		
		Apple a1 = new Apple();
		a1.setColor("green");
		a1.setWeight(3);
		
		Orange o = new Orange();
//		PropertyUtils.copyProperties(a1, a);
//		PropertyUtils.copyProperties(o, a);
		BeanUtils.copyProperties(a1, a);
		BeanUtils.copyProperties(o, a);
	}

}
