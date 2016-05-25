package cn.sam.test.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cn.sam.test.jpa.bean.Person;

public class JpaTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	@Ignore
	public void createTable() {
		// 可以验证生成表是否正确
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		factory.close();
	}

	@Test
	@Ignore
	public void save() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Person person = new Person(); // person为new状态
		person.setName("zhang san");
		em.persist(person); // 持久化实体
		em.getTransaction().commit();
		em.close();
		factory.close();
	}
	// new 、托管、脱管、删除

	@Test
	public void update() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Person person = em.find(Person.class, 1);
		person.setName("hmk11"); // person为托管状态
		em.getTransaction().commit();
		em.close();
		factory.close();
	}

	@Test
	@Ignore
	public void update2() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Person person = em.find(Person.class, 1);
		em.clear(); // 把实体管理器中的所有实体变为脱管状态
		person.setName("hmk2");
		em.merge(person); // 把脱管状态变为托管状态,merge可以自动选择insert or update 数据
		em.getTransaction().commit();
		em.close();
		factory.close();
	}

	@Test
	@Ignore
	public void remove() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Person person = em.find(Person.class, 1);
		em.remove(person); // 删除实体
		em.getTransaction().commit();
		em.close();
		factory.close();
	}

	@Test
	@Ignore
	public void find() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		Person person = em.find(Person.class, 2); // 类似于hibernate的get方法,没找到数据时，返回null
		System.out.println(person.getName());
		em.close();
		factory.close();
	}

	@Test
	@Ignore
	public void find2() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		Person person = em.getReference(Person.class, 2); // 类似于hibernate的load方法,延迟加载.没相应数据时会出现异常
		System.out.println(person.getName()); // 真正调用时才查找数据
		em.close();
		factory.close();
	}
}
