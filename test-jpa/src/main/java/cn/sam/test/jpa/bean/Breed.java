package cn.sam.test.jpa.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;

/**
 * 品种
 * @author SAM
 *
 */
@Entity
@NamedEntityGraph(name = "Breed.detail", attributeNodes = @NamedAttributeNode("members"))
public class Breed {
	private Integer id;
	private String name;
	
	// default fetch mode is lazy.
	@ManyToMany
	private List<Animal> members = new ArrayList<>();

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(length = 12)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
