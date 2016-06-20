package cn.sam.test.jpa.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import cn.sam.test.jpa.bean.Person;

/**
 * 1、Spring jpa能够根据接口方法名、命名查询、Query注解自动实现方法细节，而PersonRepositoryCustom则用于实现自定义方法逻辑
 * <p>
 * 2、PersonRepositoryCustom的实现类名默认是JPA接口名+后缀Ipml，如PersonRepositoryImpl，如果要定义后缀，可以配置jpa:repositories的repository-impl-postfix属性。
 * 实现类名也可以完全使用自定义的名称，具体请见官方文档：http://docs.spring.io/spring-data/data-jpa/docs/1.10.1.RELEASE/reference/html/
 * <p>
 * 3、JpaRepository、PagingAndSortingRepository、CrudRepository等接口里面的方法应该是在SimpleJpaRepository类中实现的
 * 
 * @author SAM
 *
 */
public interface PersonRepository extends JpaRepository<Person, Integer>, PersonRepositoryCustom {
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
	
	// 分页
	Page<Person> findByLastname(String lastname, Pageable pageable);
//	Slice<Person> findByLastname(String lastname, Pageable pageable);
//	List<User> findByLastname(String lastname, Pageable pageable);

	// 排序
	List<User> findByLastname(String lastname, Sort sort);
	
	// first、top
	User findFirstByOrderByLastnameAsc();
	User findTopByOrderByAgeDesc();
	Page<User> queryFirst10ByLastname(String lastname, Pageable pageable);
	Slice<User> findTop3ByLastname(String lastname, Pageable pageable);
	List<User> findFirst10ByLastname(String lastname, Sort sort);
	List<User> findTop10ByLastname(String lastname, Pageable pageable);
	
	// Stream
	@Query("select u from User u")
	Stream<Person> findAllByCustomQueryAndStream();
	
	// Async
	@Async
	Future<User> findByFirstname(String firstname);               
	@Async
	CompletableFuture<User> findOneByFirstname(String firstname); 
	@Async
	ListenableFuture<User> findOneByLastname(String lastname); 
}
