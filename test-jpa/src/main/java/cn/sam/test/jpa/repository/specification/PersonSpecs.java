package cn.sam.test.jpa.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.sam.test.jpa.bean.Person;

public class PersonSpecs {

	public static Specification<Person> isNameStartWith(String name) {
		return new Specification<Person>() {
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.like(root.get(Person_.createdAt), pattern);
			}
		};
	}

	public static Specification<Person> isMobileEq(String mobile) {
		return new Specification<Person>() {

			@Override
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return null;
			}
		};
	}
}