package cn.sam.test.jpa.repository.impl;

import java.util.ArrayList;
import java.util.List;

import cn.sam.test.jpa.bean.Person;
import cn.sam.test.jpa.repository.PersonRepositoryCustom;

public class PersonRepositoryImpl implements PersonRepositoryCustom {

	@Override
	public List<Person> testCustom(String xxx) {
		List<Person> list = new ArrayList<>();
		Person p = new Person();
		p.setName(xxx);
		list.add(p);
		return list;
	}

}
