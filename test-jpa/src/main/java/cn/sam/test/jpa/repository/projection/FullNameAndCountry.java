package cn.sam.test.jpa.repository.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * See {@link NoAddresses} used in {@link PersonRepository}
 * @author SAM
 *
 */
public interface FullNameAndCountry {

	@Value("#{target.firstName} #{target.lastName}")
	String getFullName();

	@Value("#{target.address.country}")
	String getCountry();
}
