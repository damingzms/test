package cn.sam.test.springboot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class Application {
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("")
	@ResponseBody
	public Object operate(String operand1, String operand2) {
		String result = null;
		if (StringUtils.isBlank(operand1) || StringUtils.isBlank(operand2)) {
			result = "Parameters [operand1] and [operand2] can not be blank.";
			return result;
		}
		try {
			double op1d = Double.valueOf(operand1);
			double op2d = Double.valueOf(operand2);
			result = String.valueOf(op1d + op2d);
		} catch (Exception e) {
			LOGGER.info("not a number.");
			result = operand1 + operand2;
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}