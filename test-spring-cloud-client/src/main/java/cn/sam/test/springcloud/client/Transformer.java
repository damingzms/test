package cn.sam.test.springcloud.client;

import java.net.URI;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Aspect
@Component
public class Transformer {
	
	@Autowired
	private ServiceFactory factory;

	@Around("execution(* cn.sam.test.springcloud.client.clients..*.*(..))")
	public Object transform(ProceedingJoinPoint pjp) throws Exception {
		Object service = pjp.getTarget();
		String classSimpleName = AopUtils.getTargetClass(service).getSimpleName();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		
		// uri
		String methodName = signature.getName();
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme(factory.getProtocol())
				.host(factory.getHost())
				.port(factory.getPort())
				.pathSegment(factory.getPathPrefix(), StringUtils.uncapitalize(classSimpleName), methodName)
				.build()
				.encode();;
		URI uri = uriComponents.toUri();
		
		// request
		Object request = null;
		Object[] args = pjp.getArgs();
		if (args != null && args.length > 0) {
			request = args[0];
		}
		
		// return type
		Class<?> declaringType = signature.getReturnType();

		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		Object result = restTemplate.postForObject(uri, request, declaringType);
		return result;
	}
	
}
