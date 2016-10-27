package cn.sam.test.jpa.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, Address> address;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, String> mobile;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> lastname;

}

