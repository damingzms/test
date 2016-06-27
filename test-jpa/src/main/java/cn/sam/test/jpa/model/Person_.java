package cn.sam.test.jpa.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Person.class)
public abstract class Person_ {

	public static volatile SingularAttribute<Person, String> firstname;
	public static volatile SingularAttribute<Person, String> emailAddress;
	public static volatile SingularAttribute<Person, Address> address;
	public static volatile SingularAttribute<Person, String> name;
	public static volatile SingularAttribute<Person, String> mobile;
	public static volatile SingularAttribute<Person, Integer> id;
	public static volatile SingularAttribute<Person, String> lastname;

}

