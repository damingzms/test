package cn.sam.test.jpa.run;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;

import cn.sam.test.jpa.model.Animal;
import cn.sam.test.jpa.model.Person;
import cn.sam.test.jpa.repository.AnimalRepository;
import cn.sam.test.jpa.repository.PersonRepository;
import cn.sam.test.jpa.repository.specification.PersonSpecs;

public class JpaTest {
	
	public static JpaTest instance = new JpaTest();
	
	public static final String SPRING_CONTEXT_CONFIG = "applicationContext.xml";
	
	public static final String SPRING_CONTEXT_CONFIG_SIMPLE = "applicationContext-minimal.xml";
	
	private boolean isPure;
	
	private boolean isQuery;
	
	private EntityManagerFactory factory;
	
	private EntityManager em;
	
	private ClassPathXmlApplicationContext context;
	
	private PersonRepository personRepository;
	
	private AnimalRepository animalRepository;
	
	public static void main(String[] args) {
		instance.init(false, false, true);
		instance.testCommon();
		instance.destroy();
	}
	
	public void init(boolean isPure, boolean isQuery, boolean isMinimal) {
		this.isPure = isPure;
		this.isQuery = isQuery;
		if (this.isPure) {
			factory = Persistence.createEntityManagerFactory("oracleJPA");
			em = factory.createEntityManager();
			if (!this.isQuery) {
				em.getTransaction().begin();
			}
		} else {
			context = new ClassPathXmlApplicationContext("classpath:" + (isMinimal ? SPRING_CONTEXT_CONFIG_SIMPLE : SPRING_CONTEXT_CONFIG));
			personRepository = (PersonRepository) context.getBean("personRepository");
			animalRepository = (AnimalRepository) context.getBean("animalRepository");
		}
	}
	
	public void destroy() {
		if (this.isPure) {
			if (!this.isQuery) {
				em.getTransaction().commit();
			}
			em.close();
			factory.close();
		} else {
//			context.close();
		}
	}

	// Spring data jpa
	
	public void createTable() {
		// 可以验证生成表是否正确
	}

	public void save() {
		Person person = new Person(); // person为new状态
		person.setName("zhang san1");
		if (this.isPure) {
			em.persist(person);
		} else {
			personRepository.save(person);
		}
		
		Animal animal = new Animal();
		animal.setName("dobby");
		if (this.isPure) {
			em.persist(animal);
		} else {
			animalRepository.save(animal);
		}
	}

	public void update() {
		Person person = null;
		if (this.isPure) {
			person = em.find(Person.class, 2);
			person.setName(StringUtils.reverse(person.getName())); // person为托管状态
		} else {
			person = personRepository.findOne(1);
			person.setName(StringUtils.reverse(person.getName()));
			personRepository.save(person);
		}
	}

	public void testCustom() {
		List<Person> list = personRepository.testCustom("testCustom");
		Person person = personRepository.findOne(11);
	}
	
	public void testCommon() {
		List<Animal> a = animalRepository.findByName("dobby");
	}
	
	public void testPaging() {
		Page<Person> page = personRepository.findAll(new PageRequest(1, 20));
	}
	
	public void testSpecification() {
		List<Person> list = personRepository.findAll(PersonSpecs.isNameStartWith("sam"));
		List<Person> list2 = personRepository.findAll(Specifications.where(PersonSpecs.isNameStartWith("sam")).and(PersonSpecs.isMobileEq("13755555555")));
	}
	
	public void testExample() {
		// example and matcher
		Person person = new Person();
		person.setFirstname("Dave");
		Example<Person> example = Example.of(person);

		Person person1 = new Person();
		person1.setFirstname("Dave");
		ExampleMatcher matcher1 = ExampleMatcher.matching().withIgnorePaths("lastname").withIncludeNullValues();
		Example<Person> example1 = Example.of(person1, matcher1);

		ExampleMatcher matcher2 = ExampleMatcher.matching()
				.withMatcher("address.zipCode", GenericPropertyMatchers.startsWith())
				.withMatcher("firstname", GenericPropertyMatchers.endsWith())
				.withMatcher("lastname", GenericPropertyMatchers.startsWith().ignoreCase());

		ExampleMatcher matcher3 = ExampleMatcher.matching()
				.withMatcher("firstname", match -> match.endsWith())
				.withMatcher("firstname", match -> match.startsWith());
		
		List<Person> list = personRepository.findAll(example);
	}
	
	
	
	
	
	// 以下方法只使用原始jpa api
	
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

	public void find() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		Person person = em.find(Person.class, 2); // 类似于hibernate的get方法,没找到数据时，返回null
		System.out.println(person.getName());
		em.close();
		factory.close();
	}

	public void find2() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracleJPA");
		EntityManager em = factory.createEntityManager();
		Person person = em.getReference(Person.class, 2); // 类似于hibernate的load方法,延迟加载.没相应数据时会出现异常
		System.out.println(person.getName()); // 真正调用时才查找数据
		em.close();
		factory.close();
	}
}
