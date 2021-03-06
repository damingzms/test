package cn.sam.test.test.spring.boot.client.service;

import cn.sam.test.test.spring.boot.client.dto.Response;
import cn.sam.test.test.spring.boot.client.dto.Role;
import cn.sam.test.test.spring.boot.client.dto.User;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequestMapping("/example/")
public class PathExampleService {
  @RequestMapping("testReturnVoid")
  public void testReturnVoid() {
  }

  @RequestMapping("testCollection")
  public Response testCollection(@RequestBody List<User> arg0) {
    return null;
  }

  @RequestMapping("testObject")
  public Response testObject(@RequestBody User arg0) {
    return null;
  }

  @RequestMapping("testObjectAndString")
  public Response testObjectAndString(@RequestBody User arg0,
      @RequestParam("newName") String newName) {
    return null;
  }

  @RequestMapping(
      method = RequestMethod.GET,
      value = "testRequestMethod"
  )
  public Response testRequestMethod(@RequestParam("userName") String userName) {
    return null;
  }

  @RequestMapping(
      method = RequestMethod.GET,
      value = "testPathVariable/{userName}"
  )
  public String testPathVariable(@PathVariable String arg0) {
    return null;
  }

  @RequestMapping("testMultiObject")
  public Response testMultiObject(@RequestBody User arg0, Role arg1) {
    return null;
  }

  @RequestMapping("testString")
  public String testString(@RequestParam("name") String name) {
    return null;
  }

  @RequestMapping("testReturnPrimitive")
  public int testReturnPrimitive() {
    return 0;
  }

  @RequestMapping("testMap")
  public Response testMap(@RequestBody Map<String, String> arg0) {
    return null;
  }

  @RequestMapping("testArray")
  public Response testArray(@RequestParam("userNames") String[] userNames) {
    return null;
  }

  @RequestMapping(
      method = RequestMethod.GET,
      value = "test"
  )
  public String test() {
    return null;
  }

  @RequestMapping("testLong")
  public String testLong(@RequestParam("name") Long name) {
    return null;
  }
}
