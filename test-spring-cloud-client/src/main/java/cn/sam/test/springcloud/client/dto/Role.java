package cn.sam.test.springcloud.client.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Role {
	
	private String roleName;
	
	private Date createdDate;
	
//	private User user;
	
}
