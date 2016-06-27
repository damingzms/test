package cn.sam.test.jpa.repository.projection;

import org.springframework.beans.factory.annotation.Value;

import cn.sam.test.jpa.repository.PersonRepository;

/**
 * Used in {@link PersonRepository}
 * @author SAM
 *
 */
public interface NoAddresses {

	String getFirstName();

	String getLastName();
	
	@Value("#{(target.mobile == null || target.mobile.empty) ? null : '***********'}")
	String getMobile();
}
