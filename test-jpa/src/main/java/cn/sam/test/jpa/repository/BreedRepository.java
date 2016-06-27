package cn.sam.test.jpa.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import cn.sam.test.jpa.model.Breed;

/**
 * 详细用法请见{@link PersonRepository}
 * 
 * @author SAM
 *
 */
public interface BreedRepository extends CommonRepository<Breed, Integer> {
	
	//  Fetch and LoadGraphs
	@EntityGraph(value = "Breed.detail", type = EntityGraphType.LOAD)
	Breed getByName(String name);

//	@EntityGraph(attributePaths = { "members" })
//	Breed getByName(String name);
	
}
