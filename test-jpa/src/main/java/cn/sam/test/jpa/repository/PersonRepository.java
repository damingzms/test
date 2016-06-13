package cn.sam.test.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sam.test.jpa.bean.Person;

/**
 * Spring jpa能够根据接口方法名、命名查询、Query注解自动实现方法细节，而PersonRepositoryCustom则用于实现自定义方法逻辑
 * PersonRepositoryCustom的实现类名默认是JPA接口名+后缀Ipml，如PersonRepositoryImpl，如果要定义后缀，可以配置jpa:repositories的repository-impl-postfix属性
 * 实现类名也可以完全使用自定义的名称，具体请见官方文档：http://docs.spring.io/spring-data/data-jpa/docs/1.0.0.M1/reference/html/
 * @author SAM
 *
 */
public interface PersonRepository extends JpaRepository<Person, Integer>, PersonRepositoryCustom {

	List<Person> findAll(Specification<Person> spec);
	
}
