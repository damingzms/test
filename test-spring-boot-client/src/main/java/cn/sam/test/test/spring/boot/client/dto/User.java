package cn.sam.test.test.spring.boot.client.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Person {
  private Date birthday;

  private Role role;
}
