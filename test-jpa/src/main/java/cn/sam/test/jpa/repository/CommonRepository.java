package cn.sam.test.jpa.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 定义公共的方法
 * <p />
 * 同时这种方式也可以实现自定义方法逻辑，具体见官方文档：http://docs.spring.io/spring-data/data-jpa/docs/1.0.0.M1/reference/html/
 * 
 * @author SAM
 *
 */
@NoRepositoryBean
public interface CommonRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
	
	public List<T> findByName(String name);
	
}
