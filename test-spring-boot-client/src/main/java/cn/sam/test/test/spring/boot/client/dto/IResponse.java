package cn.sam.test.test.spring.boot.client.dto;

import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;

public interface IResponse {
  String RESPONSE_CODE = "100";

  int MAX_COUNT = new Integer("50");

  long MAX_COUNT_LONG = new Long("5000000000000000");

  Integer MUN = new Integer("50");

  Double d1 = new Double("5.01");

  double d2 = new Double("9.0");

  Gender DEFAULT_GENDER = Gender.MALE;

  EnumComplex COMPLEX = EnumComplex.Const2;
}
