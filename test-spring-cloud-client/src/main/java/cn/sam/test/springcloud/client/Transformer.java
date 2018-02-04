package cn.sam.test.springcloud.client;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
	
	/**
	 * 这里不使用ConcurrentHashMap。因为与其每次调用都需要运行一段同步代码块，不如初始的几次调用重复获取数据。这里的重复使用反射获取数据，不会产生问题
	 */
	private Map<String, URI> uriCache = new HashMap<>();
	
	private Map<String, Class<?>> responseTypeCache = new HashMap<>();
	
	@Autowired
	private ServiceFactory factory;

	@Around("execution(* cn.sam.test.springcloud.client.service.*.*(..))")
	public Object transform(ProceedingJoinPoint pjp) throws Exception {
		Object service = pjp.getTarget();
		Class<?> serviceClass = AopUtils.getTargetClass(service);
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		String serviceClassName = serviceClass.getName();
		String methodName = method.getName();
		String cacheKey = serviceClassName + "." + methodName;
		
		// uri
		URI uri = uriCache.get(cacheKey);
		if (uri == null) {
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
			String[] paths = new String[3];
			paths[0] = factory.getBasePath();
			paths[1] = classPath;
			paths[2] = methodPath;
			UriComponents uriComponents = UriComponentsBuilder.newInstance()
					.scheme(factory.getProtocol())
					.host(factory.getHost())
					.port(factory.getPort())
					.pathSegment(factory.getBasePath(), classPath, methodPath)
					.build()
					.encode();
			uri = uriComponents.toUri();
			uriCache.put(cacheKey, uri);
		}
		
		// return type
		Class<?> responseType = responseTypeCache.get(cacheKey);
		if (responseType == null) {
			responseType = signature.getReturnType();
			responseTypeCache.put(cacheKey, responseType);
		}
		
		// request, TODO 多参数支持
		Object request = null;
		Object[] args = pjp.getArgs();
		if (args != null && args.length > 0) {
			request = args[0];
		}

		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		Object result = restTemplate.postForObject(uri, request, responseType);
		return result;
	}
	
}
