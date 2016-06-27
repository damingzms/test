package cn.sam.test.jpa.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.sam.test.jpa.model.Person;
import cn.sam.test.jpa.model.Person_;

/**
 * Person_为自动生成的资源，见pom.xml build plugins <br />
 * 官方文档：http://docs.jboss.org/hibernate/jpamodelgen/1.3/reference/en-US/html/index.html
 * @author SAM
 *
 */
public class PersonSpecs {

	public static Specification<Person> isNameStartWith(String name) {
		return new Specification<Person>() {
			
			@Override
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get(Person_.name), name + "%");
			}
		};
	}

	public static Specification<Person> isMobileEq(String mobile) {
		return new Specification<Person>() {

			@Override
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Person_.mobile), mobile);
			}
		};
	}
}