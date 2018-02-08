package cn.sam.test.springboot.bean;

import java.util.Date;

public class User extends Person {
	
	private Date birthday;
	
	private Role role;

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
