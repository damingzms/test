package cn.sam.commontest.beanutils.dozer;

import org.dozer.DozerBeanMapper;

public class Test {

	public static void main(String[] args) {
		Father f = new Father();
		f.setJob("engineer");
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		Son son = mapper.map(f, Son.class);
		System.out.println(son.getJob());
	}

}
