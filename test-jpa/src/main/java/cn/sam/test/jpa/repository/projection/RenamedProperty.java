package cn.sam.test.jpa.repository.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * See {@link NoAddresses} used in {@link PersonRepository}
 * @author SAM
 *
 */
public interface RenamedProperty {

	String getFirstName();

	@Value("#{target.lastName}")
	String getName();
}
