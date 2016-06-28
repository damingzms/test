package cn.sam.test.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;

@Entity
@NamedQuery(name = "User.findByEmailAddress", query = "select p from Person p where p.emailAddress = ?1")
@NamedStoredProcedureQuery(name = "Person.plus1io", procedureName = "plus1inout", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "arg", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "res", type = Integer.class) })
public class Person {
	private Integer id;
	private String name;
	private String firstname;
	private String lastname;
	private String emailAddress;
	private String mobile;

	private Address address;

	/* Auditing start */
	@CreatedBy
	private User createdBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedBy
	private User lastModifiedBy;

	@LastModifiedDate
	private Date lastModifiedDate;
	
	public static class SpringSecurityAuditorAware implements AuditorAware<User> {
		public User getCurrentAuditor() {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			if (authentication == null || !authentication.isAuthenticated()) {
//				return null;
//			}
//			return ((MyUserDetails) authentication.getPrincipal()).getUser();
			return new User();
		}
	}
	/* Auditing end */

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

	@Column(length = 12)
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(length = 12)
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(length = 30)
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(length = 15)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ManyToOne
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}