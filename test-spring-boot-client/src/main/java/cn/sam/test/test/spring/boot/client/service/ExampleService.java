package cn.sam.test.test.spring.boot.client.service;

import java.lang.String;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class ExampleService {
  @RequestMapping("/")
  public String home() {
    return null;
  }
}
