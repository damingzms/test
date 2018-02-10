package cn.sam.test.test.spring.boot.client;

import cn.sam.test.test.spring.boot.client.service.ExampleService;
import cn.sam.test.test.spring.boot.client.service.PathExampleService;
import java.lang.String;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class TestSpringBootServiceFactory implements ServiceFactory {
  private String protocol = "http";

  private String host = "127.0.0.1";

  private int port = -1;

  private String basePath = "";

  @Autowired
  private PathExampleService PathExampleService;

  @Autowired
  private ExampleService ExampleService;
}
