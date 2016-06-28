package cn.sam.test.jpa.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import cn.sam.test.jpa.model.Person;
import cn.sam.test.jpa.repository.projection.NoAddresses;

/**
 * 1、Spring jpa能够根据接口方法名、命名查询、Query注解自动实现方法细节，而PersonRepositoryCustom则用于实现自定义方法逻辑
 * <p>
 * 2、PersonRepositoryCustom的实现类名默认是JPA接口名+后缀Ipml，如PersonRepositoryImpl，如果要定义后缀，可以配置jpa:repositories的repository-impl-postfix属性。
 * 实现类名也可以完全使用自定义的名称，具体请见官方文档：http://docs.spring.io/spring-data/data-jpa/docs/1.10.1.RELEASE/reference/html/
 * <p>
 * 3、JpaRepository、PagingAndSortingRepository、CrudRepository等接口里面的方法应该是在SimpleJpaRepository类中实现的
 * <p>
 * 4、extending your repository interface with the JpaSpecificationExecutor interface to support specifications, <br />
 * see also {@link cn.sam.test.jpa.repository.specification.PersonSpecs} and {@link cn.sam.test.jpa.run.JpaTest#testSpecification()}
 * <p>
 * 5、query by example，please refer to {@link cn.sam.test.jpa.run.JpaTest#testExample()}
 * <p>
 * 6、Transaction with readOnly flag is propagated as hint to the underlying JDBC driver for performance optimizations.
 * Furthermore, Spring will perform some optimizations on the underlying JPA provider. 
 * 
 * @author SAM
 *
 */
public interface PersonRepository extends JpaRepository<Person, Integer>, PersonRepositoryCustom, JpaSpecificationExecutor<Person> {
	List<Person> findByEmailAddressAndLastname(String emailAddress, String lastname);

	// distinct
	List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);

	List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

	// ignoring case
	List<Person> findByLastnameIgnoreCase(String lastname);
	List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

	// ORDER BY
	List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
	List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
	
	// 复杂属性
	List<Person> findByAddressZipCode(String zipCode);
	List<Person> findByAddress_ZipCode(String zipCode);
	
	// 分页（方法注释掉是因为同名冲突，下同）
	Page<Person> findByLastname(String lastname, Pageable pageable);
//	Slice<Person> findByLastname(String lastname, Pageable pageable);
//	List<Person> findByLastname(String lastname, Pageable pageable);

	// 排序
	List<Person> findByLastname(String lastname, Sort sort);
	
	// first、top
	Person findFirstByOrderByLastnameAsc();
	Person findTopByOrderByAgeDesc();
	Page<Person> queryFirst10ByLastname(String lastname, Pageable pageable);
	Slice<Person> findTop3ByLastname(String lastname, Pageable pageable);
	List<Person> findFirst10ByLastname(String lastname, Sort sort);
	List<Person> findTop10ByLastname(String lastname, Pageable pageable);
	
	// Stream
	@Query("select p from Person p")
	Stream<Person> findAllByCustomQueryAndStream();
	
	// Async
	@Async
	Future<Person> findByFirstname(String firstname);               
	@Async
	CompletableFuture<Person> findOneByFirstname(String firstname); 
	@Async
	ListenableFuture<Person> findOneByLastname(String lastname);
	
	// named query
	// see file /test-jpa/src/main/resources/META-INF/orm.xml
	// see "@NamedQuery" annotation in cn.sam.test.jpa.bean.Person
	List<Person> findByLastname(String lastname);
//	Person findByEmailAddress(String emailAddress);
	
	// Queries annotated
	// Queries annotated to the query method will take precedence over queries defined using @NamedQuery or named queries declared in orm.xml
//	@Query("select p from Person p where p.emailAddress = ?1")
//	Person findByEmailAddress(String emailAddress);
	
	/*
	 * In the just shown sample LIKE delimiter character % is recognized and the query transformed into a valid JPQL query (removing the %).
	 * Upon query execution the parameter handed into the method call gets augmented with the previously recognized LIKE pattern.
	 */
	@Query("select p from Person p where p.firstname like %?1")
	List<Person> findByFirstnameEndsWith(String firstname);
	
	@Query(value = "SELECT * FROM PERSON WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
	Person findByEmailAddress(String emailAddress);
	
	@Query("select p from Person p where p.firstname = :firstname or p.lastname = :lastname")
	Person findByLastnameOrFirstname(@Param("lastname") String lastname, @Param("firstname") String firstname);
	
	//spel-expressions
//	@Query("select p from #{#entityName} p where p.lastname = ?1")
//	List<Person> findByLastname(String lastname);
	
	
	// update
	@Modifying
	@Query("update Person p set p.firstname = ?1 where p.lastname = ?2")
	int setFixedFirstnameFor(String firstname, String lastname);
	
	
	//QueryHints
//	@QueryHints(value = { @QueryHint(name = "name", value = "value") }, forCounting = false)
//	Page<Person> findByLastname(String lastname, Pageable pageable);
	
	
	// projection
	NoAddresses findByFirstName(String firstName);
	
	
	// procedure 存储过程
	// see "@NamedStoredProcedureQuery" annotation in cn.sam.test.jpa.bean.Person
	/*
	DROP procedure IF EXISTS plus1inout
	CREATE procedure plus1inout (IN arg int, OUT res int)
	BEGIN ATOMIC
	 set res = arg + 1;
	END
	*/
	@Procedure("plus1inout")
	Integer plus1inout(Integer arg);
//	@Procedure(procedureName = "plus1inout")
//	Integer plus1inout(Integer arg);
//	@Procedure(name = "Person.plus1io")
//	Integer plus1inout(@Param("arg") Integer arg);
//	@Procedure
//	Integer plus1io(@Param("arg") Integer arg);

	// Lock
//	@Lock(LockModeType.OPTIMISTIC)
//	List<Person> findByLastname(String lastname);
	
}
