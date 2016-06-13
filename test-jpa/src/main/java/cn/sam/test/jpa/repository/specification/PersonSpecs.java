package cn.sam.test.jpa.repository.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.sam.test.jpa.bean.Person;

public class PersonSpecs {

	public static Specification<Person> isLongTermPerson() {
		return new Specification<Person>() {
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				return null;
			}
		};
	}

}