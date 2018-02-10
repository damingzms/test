package cn.sam.test.test.spring.boot.client.dto;

import java.lang.String;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response implements IResponse {
  private String result;
}
