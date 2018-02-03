package cn.sam.test.springcloud.client;

import java.lang.reflect.Method;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Aspect
@Component
public class Transformer {
	
	@Autowired
	private ServiceFactory factory;

	@Around("execution(* cn.sam.test.springcloud.client.service.*.*(..))")
	public Object transform(ProceedingJoinPoint pjp) throws Exception {
		Object service = pjp.getTarget();
		Class<?> serviceClass = AopUtils.getTargetClass(service);
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		
		// uri
		String classPath = "";
		RequestMapping clsMappingAnnotation = serviceClass.getAnnotation(RequestMapping.class);
		if (clsMappingAnnotation != null) {
			String[] values = clsMappingAnnotation.value();
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					if (!StringUtils.isEmpty(value)) {
						classPath = value;
						break;
					}
				}
			}
			if (StringUtils.isEmpty(classPath)) {
				String[] paths = clsMappingAnnotation.path();
				if (paths != null) {
					for (int i = 0; i < paths.length; i++) {
						String path = paths[i];
						if (!StringUtils.isEmpty(path)) {
							classPath = path;
							break;
						}
					}
				}
			}
		}

		String methodPath = "";
		RequestMapping methodMappingAnnotation = method.getAnnotation(RequestMapping.class);
		if (methodMappingAnnotation != null) {
			String[] values = methodMappingAnnotation.value();
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					if (!StringUtils.isEmpty(value)) {
						methodPath = value;
						break;
					}
				}
			}
			if (StringUtils.isEmpty(methodPath)) {
				String[] paths = methodMappingAnnotation.path();
				if (paths != null) {
					for (int i = 0; i < paths.length; i++) {
						String path = paths[i];
						if (!StringUtils.isEmpty(path)) {
							methodPath = path;
							break;
						}
					}
				}
			}
		}
		
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme(factory.getProtocol())
				.host(factory.getHost())
				.port(factory.getPort())
				.pathSegment(factory.getBasePath(), classPath, methodPath)
				.build()
				.encode();
		URI uri = uriComponents.toUri();
		
		// request, TODO 多参数支持
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
