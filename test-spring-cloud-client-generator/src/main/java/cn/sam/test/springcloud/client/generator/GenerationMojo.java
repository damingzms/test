package cn.sam.test.springcloud.client.generator;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Predicate;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import cn.sam.test.springcloud.client.generator.util.FileUtils;
import cn.sam.test.springcloud.client.generator.util.TemplateUtils;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE)
public class GenerationMojo extends AbstractMojo {

//	@Parameter(defaultValue = "${project}", readonly = true)
//	private MavenProject project;
	
	@Parameter(defaultValue = "${plugin}", readonly = true)
	private PluginDescriptor pluginDescriptor;

	/**
	 * 当前项目编译输出目录，不允许用户配置
	 */
	@Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
	private String currentProjectOutputDirectory;

	/**
	 * 当前项目根目录，不允许用户配置
	 */
	@Parameter(defaultValue = "${project.basedir}", readonly = true)
	private String currentProjectBaseDir;
	
	/**
	 * 扫描此包及其子包下面的Controller和DTO。多个包以“,”分开
	 */
	@Parameter(defaultValue = "${project.groupId}", required = true)
	private String packagesToScan;
	
	/**
	 * 是否生成pom.xml文件
     */
	@Parameter(defaultValue = "true", required = true)
	public boolean generatingPom;
	
	/**
	 * 是否生成DTO
     */
	@Parameter(defaultValue = "true", required = true)
	private boolean generatingDto;
	
	/**
	 * 客户端项目groupId
     */
	@Parameter(defaultValue = "${project.groupId}", required = true)
	private String groupId;
	
	/**
	 * 客户端项目artifactId
     */
	@Parameter(defaultValue = "${project.artifactId}-client", required = true)
	private String artifactId;

	/**
	 * 客户端项目version
     */
	@Parameter(defaultValue = "${project.version}", required = true)
	private String version;

	/**
	 * 生成的项目根目录。默认值：与当前项目根目录同级的名为artifactId参数值的目录
	 */
	@Parameter
	private String baseDir;
	
	/**
	 * src目录，不允许用户配置
     */
	@Parameter(defaultValue = "/src/main/java", required = true, readonly = true)
	private String srcDir;
	
	/**
	 * 生成DTO类目标包。generatingDto = true时才有效。Value中的“-”和“_”符号会替换成“.”
     */
	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}.client.dto", required = true)
	private String dtoPkg;
	
	/**
	 * 生成service类目标包。Value中的“-”和“_”符号会替换成“.”
     */
	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}.client.service", required = true)
	private String servicePkg;
	
	/**
	 * 生成factory类目标包。默认值：servicePkg的父级包
     */
	@Parameter
	private String factoryPkg;
	
	/**
	 * Value中的“-”和“_”符号会删除，单词首字母自动大写
     */
	@Parameter(defaultValue = "${project.artifactId}ServiceFactory", required = true)
	private String factoryClassName;
	
	private File baseDirFile;
	
	private File srcDirFile;
	
	private File dtoDirFile;
	
	private File serviceDirFile;
	
	private File factoryDirFile;
	
	public void execute() throws MojoExecutionException {
		Log log = getLog();
		log.info("Generating Spring Cloud client project.");
		
		// PREPARE
		dtoPkg = dtoPkg.replaceAll("[-, _]", ".");
		servicePkg = servicePkg.replaceAll("[-, _]", ".");
		factoryPkg = servicePkg.substring(0, servicePkg.lastIndexOf('.'));
		factoryClassName = WordUtils.capitalize(factoryClassName, '-', '_').replaceAll("[-, _]", "");
		
		try {
			
			// SCAN class
			ClassRealm realm = pluginDescriptor.getClassRealm();
			File elementFile = new File(currentProjectOutputDirectory); // maven插件运行时加载项目的类，但是不加载项目依赖的类
			realm.addURL(elementFile.toURI().toURL());
			
			Set<Class<?>> controllerClasses = new HashSet<>();
			Reflections reflections = new Reflections(packagesToScan);
			controllerClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
			controllerClasses.addAll(reflections.getTypesAnnotatedWith(RestController.class));
			if (CollectionUtils.isEmpty(controllerClasses)) {
				log.warn("Cannot find any Controller in package or sub-package: " + packagesToScan);
				return;
			}
			
			// GENERATE Project Structure
			genProjectStructure();
			
			// GENERATE POM
			genPom();
			
			// JAVAFILES
			Map<Class<?>, JavaFile> dtoJavaFileMap = new HashMap<>();
			Map<Class<?>, ClassName> dtoClassNameMap = new HashMap<>();
			List<JavaFile> serviceJavaFiles = new ArrayList<>();
			Map<String, Integer> dtoNameCountMap = new HashMap<>();
			Map<String, Integer> serviceNameCountMap = new HashMap<>();
			for (Class<?> controllerClasse : controllerClasses) {
				
				// SERVICE
				// service class
				String serviceName = controllerClasse.getSimpleName();
				if (serviceName.endsWith("Controller")) {
					serviceName = serviceName.substring(0, serviceName.lastIndexOf("Controller"));
				} 
				if (!serviceName.endsWith("Service")) {
					serviceName = serviceName + "Service";
				}
				Integer serviceNameCount = serviceNameCountMap.get(serviceName);
				if (serviceNameCount == null || serviceNameCount == 0) {
					serviceNameCount = 1;
					serviceNameCountMap.put(serviceName, serviceNameCount);
				} else {
					serviceNameCount += 1;
					serviceNameCountMap.put(serviceName, serviceNameCount);
					serviceName += serviceNameCount;
				}
				Builder serviceTypeSpecBuilder = TypeSpec.classBuilder(serviceName)
					    .addModifiers(Modifier.PUBLIC)
					    .addAnnotation(Service.class);
				
				// service class annotation
				RequestMapping classRequestMappingAnno = controllerClasse.getAnnotation(RequestMapping.class);
				if (classRequestMappingAnno != null) {
					AnnotationSpec requestMappingAnnotationSpec = AnnotationSpec.get(classRequestMappingAnno);
					serviceTypeSpecBuilder.addAnnotation(requestMappingAnnotationSpec);
				}
				
				// service method
				Predicate<Method> methodPredicate = ReflectionUtils.withAnnotation(RequestMapping.class);
				@SuppressWarnings("unchecked")
				Set<Method> methods = ReflectionUtils.getMethods(controllerClasse, methodPredicate);
				if (CollectionUtils.isEmpty(methods)) {
					log.warn("Cannot find any mapping method in class: " + controllerClasse.getName());
				} else {
					for (Method method : methods) {
						
						// RESPONSE DTO
						Class<?> returnType = method.getReturnType();
						TypeName returnClassName = genDtoJavaFile(dtoJavaFileMap, dtoClassNameMap, dtoNameCountMap, returnType);;
						
						// service method
						com.squareup.javapoet.MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(method.getName())
								.addModifiers(Modifier.PUBLIC).returns(returnClassName);
						
						// service method annotation
						RequestMapping methodRequestMappingAnno = method.getAnnotation(RequestMapping.class);
						AnnotationSpec requestMappingAnnotationSpec = AnnotationSpec.get(methodRequestMappingAnno);
						methodSpecBuilder.addAnnotation(requestMappingAnnotationSpec);

						// service method parameter
						java.lang.reflect.Parameter[] parameters = method.getParameters();
						if (parameters != null && parameters.length > 0) {
							List<ParameterSpec> parameterSpecList = new ArrayList<>();
							for (java.lang.reflect.Parameter parameter : parameters) {
								
								// REQUEST DTO
								Class<?> parameterType = parameter.getType();
								TypeName parameterClassName = genDtoJavaFile(dtoJavaFileMap, dtoClassNameMap, dtoNameCountMap, parameterType);
								
								// service method parameter
								String name = null;
								RequestParam requestParamAnno = parameter.getAnnotation(RequestParam.class);
								if (requestParamAnno != null) {
									name = requestParamAnno.value();
									if (StringUtils.isEmpty(name)) {
										name = requestParamAnno.name();
									}
								}
								if (StringUtils.isEmpty(name)) {
									name = parameter.getName();
								}
								com.squareup.javapoet.ParameterSpec.Builder parameterSpecBuilder = ParameterSpec.builder(parameterClassName, name, new Modifier[]{});
								
								// service method parameter annotation
								List<AnnotationSpec> parameterAnnotationSpecList = new ArrayList<>();
								RequestBody requestBodyAnno = parameter.getAnnotation(RequestBody.class);
								if (requestParamAnno != null) {
									AnnotationSpec requestParamAnnoSpec = AnnotationSpec.get(requestParamAnno);
									parameterAnnotationSpecList.add(requestParamAnnoSpec);
								}
								if (requestBodyAnno != null) {
									AnnotationSpec requestBodyAnnoSpec = AnnotationSpec.get(requestBodyAnno);
									parameterAnnotationSpecList.add(requestBodyAnnoSpec);
								}
								if (!parameterAnnotationSpecList.isEmpty()) {
									parameterSpecBuilder.addAnnotations(parameterAnnotationSpecList);
								}
								
								parameterSpecList.add(parameterSpecBuilder.build());
							}
							methodSpecBuilder.addParameters(parameterSpecList);
						}

						// service method code
						if (returnType != void.class) {
							methodSpecBuilder.addStatement("return null");
						}
						
						serviceTypeSpecBuilder.addMethod(methodSpecBuilder.build());
					}
				}
				
				TypeSpec serviceTypeSpec = serviceTypeSpecBuilder.build();
				JavaFile serviceJavaFile = JavaFile.builder(servicePkg, serviceTypeSpec).build();
				serviceJavaFiles.add(serviceJavaFile);
			}
			// GENERATE DTOs
			outputJavaFiles(srcDirFile, dtoJavaFileMap.values());
			
			// GENERATE Services
			outputJavaFiles(srcDirFile, serviceJavaFiles);
			
			// GENERATE Factory
			
			// GENERATE Util
			
			// GENERATE Transformer

			log.info("Successfully Generated Spring Cloud client project: " + baseDirFile.getAbsolutePath());
		} catch (Throwable e) {
			log.error(e);
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	private void genProjectStructure() throws Exception {
		if (baseDir == null) {
			baseDirFile = new File(currentProjectBaseDir, ".." + File.separator + artifactId);
			baseDir = baseDirFile.getPath();
		} else {
			baseDirFile = new File(baseDir);
		}
		srcDirFile = FileUtils.mkdirs(baseDirFile, srcDir);
		dtoDirFile = FileUtils.mkdirs(srcDirFile, dtoPkg.replace('.', File.separatorChar));
		serviceDirFile = FileUtils.mkdirs(srcDirFile, servicePkg.replace('.', File.separatorChar));
		factoryDirFile = new File(srcDirFile, factoryPkg.replace('.', File.separatorChar));
	}
	
	private TypeName genDtoJavaFile(Map<Class<?>, JavaFile> javaFileMap, Map<Class<?>, ClassName> classNameMap, Map<String, Integer> nameCountMap, Class<?> clazz) throws Exception {
		if (classNameMap.containsKey(clazz)) {
			return classNameMap.get(clazz);
		}
		String fullName = clazz.getName();
		if (!fullName.startsWith(packagesToScan) || fullName.startsWith("java") || fullName.startsWith("javax")
				|| fullName.startsWith("org.springframework")) {
			return TypeName.get(clazz);
		}
		
		// name
		String dtoName = clazz.getSimpleName();
		Integer dtoNameCount = nameCountMap.get(dtoName);
		if (dtoNameCount == null || dtoNameCount == 0) {
			dtoNameCount = 1;
			nameCountMap.put(dtoName, dtoNameCount);
		} else {
			dtoNameCount += 1;
			nameCountMap.put(dtoName, dtoNameCount);
			dtoName += dtoNameCount;
		}
		
		// type
		ClassName className = ClassName.get(dtoPkg, dtoName);
		classNameMap.put(clazz, className); // 此语句需要放到递归调用之前，否则互相嵌套的DTO类会出现死循环
		Builder dtoTypeSpecBuilder = null;
		if (clazz.isInterface()) {
			dtoTypeSpecBuilder = TypeSpec.interfaceBuilder(dtoName);
		} else if (clazz.isEnum()) {
			dtoTypeSpecBuilder = TypeSpec.enumBuilder(dtoName).addAnnotation(Getter.class).addAnnotation(AllArgsConstructor.class);
		} else {
			dtoTypeSpecBuilder = TypeSpec.classBuilder(dtoName).addAnnotation(Getter.class).addAnnotation(Setter.class);
		}
		dtoTypeSpecBuilder.addModifiers(Modifier.PUBLIC);
		
		// super type
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces != null && interfaces.length > 0) {
			for (Class<?> superInterface : interfaces) {
				TypeName superClassName = genDtoJavaFile(javaFileMap, classNameMap, nameCountMap, superInterface);
				dtoTypeSpecBuilder.addSuperinterface(superClassName);
			}
		}
		if (!clazz.isEnum()) {  // only classes have super classes, not ENUM
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				TypeName superClassName = genDtoJavaFile(javaFileMap, classNameMap, nameCountMap, superclass);
				dtoTypeSpecBuilder.superclass(superClassName);
			}
		}
		
		// field
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			List<FieldSpec> fieldSpecList = new ArrayList<>();
			for (Field field : fields) {
				
				// modifier
				List<Modifier> modifierList = new ArrayList<>();
				int modifiers = field.getModifiers();
				if (java.lang.reflect.Modifier.isPublic(modifiers)) modifierList.add(Modifier.PUBLIC);
				if (java.lang.reflect.Modifier.isPrivate(modifiers)) modifierList.add(Modifier.PRIVATE);
				if (java.lang.reflect.Modifier.isProtected(modifiers)) modifierList.add(Modifier.PROTECTED);
				if (java.lang.reflect.Modifier.isStatic(modifiers)) {
					modifierList.add(Modifier.STATIC);
					
					// 排除掉Enum的Constant和一些原生的属性
					if (clazz.isEnum()) {
						continue;
					}
				}
				if (java.lang.reflect.Modifier.isFinal(modifiers)) modifierList.add(Modifier.FINAL);
				
				// field type
				Class<?> fieldType = field.getType();
				TypeName fieldClassName = genDtoJavaFile(javaFileMap, classNameMap, nameCountMap, fieldType);
				
				// name
				String name = field.getName();
				
				com.squareup.javapoet.FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(fieldClassName, name, modifierList.toArray(new Modifier[]{}));
				
				// value，只支持静态成员属性，只支持原始类型、包装类和、String、Enum
				if (modifierList.contains(Modifier.STATIC)) {
					Object value = field.get(null);
					if (value != null) {
						if (ClassUtils.isPrimitiveOrWrapper(fieldType) || fieldType == String.class) {
							fieldSpecBuilder.initializer("new $T($S)", fieldClassName, value.toString());
						} else if (fieldType.getSuperclass() == Enum.class) {
							fieldSpecBuilder.initializer("$T.$L", fieldClassName, ((Enum<?>) value).name());
						}
					}
				}
				
				fieldSpecList.add(fieldSpecBuilder.build());
			}
			dtoTypeSpecBuilder.addFields(fieldSpecList);
		}
		
		if (clazz.isEnum()) {
			Enum<?>[] enumConstants = (Enum<?>[]) clazz.getEnumConstants();
			if (enumConstants != null) {
				for (Enum<?> enumConstant : enumConstants) {
					List<String> formats = new ArrayList<>();
					List<Object> args = new ArrayList<>();
					if (fields != null && fields.length > 0) {
						for (Field field : fields) {
							int modifiers = field.getModifiers();
							if (java.lang.reflect.Modifier.isStatic(modifiers)) {
								continue;
							}
							Object value = field.get(null);
							if (value != null) {
								Class<?> fieldType = field.getType();
								args.add(classNameMap.get(fieldType));
								if (ClassUtils.isPrimitiveOrWrapper(fieldType) || fieldType == String.class) {
									formats.add("new $T($S)");
									args.add(value.toString());
								} else if (fieldType.getSuperclass() == Enum.class) {
									formats.add("$T.$L");
									args.add(((Enum<?>) value).name());
								}
							} else {
								formats.add("null");
							}
						}
					}
					dtoTypeSpecBuilder.addEnumConstant(enumConstant.name(), TypeSpec.anonymousClassBuilder(StringUtils.join(formats, ", "), args.toArray()).build());
				}
			}
		}
		
		TypeSpec typeSpec = dtoTypeSpecBuilder.build();
		JavaFile javaFile = JavaFile.builder(dtoPkg, typeSpec).build();
		javaFileMap.put(clazz, javaFile);
		return className;
	}
	
	private void genPom() throws Exception {
		Map<String, Object> root = new HashMap<>();
		root.put("groupId", groupId);
		root.put("artifactId", artifactId);
		root.put("version", version + 11);
		outputFileFromTemplate(baseDirFile, TemplateUtils.TEMPLATE_NAME_POM, root);
	}
	
	private void outputFileFromTemplate(File dir, String templateName, Object dataModel) throws Exception {
		Template template = TemplateUtils.getFreemarkerConfiguration().getTemplate(templateName);
		File file = new File(dir, TemplateUtils.templateName2FileName(templateName));
	    try (Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
			template.process(dataModel, writer);
	    }
	}
	
	private void outputJavaFiles(File dir, Collection<JavaFile> javaFiles) throws Exception {
		for (JavaFile javaFile : javaFiles) {
			javaFile.writeTo(dir);
		}
	}
	
}