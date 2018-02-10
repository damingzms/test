package cn.sam.test.test.spring.boot.client.dto;

import java.lang.String;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {
  private String roleName;

  private Date createdDate;

  private List<User> user;
}
