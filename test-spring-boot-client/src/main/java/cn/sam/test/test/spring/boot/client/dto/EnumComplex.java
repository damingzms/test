package cn.sam.test.test.spring.boot.client.dto;

import java.lang.Integer;
import java.lang.String;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumComplex {
  Const1("Const1", new Integer("1")),

  Const2("Const2", new Integer("2"));

  private String str;

  private int i;
}
