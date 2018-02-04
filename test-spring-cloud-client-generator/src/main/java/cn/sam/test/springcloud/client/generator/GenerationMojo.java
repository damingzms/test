package cn.sam.test.springcloud.client.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate")
public class GenerationMojo extends AbstractMojo {
	
	/**
	 * 是否生成pom.xml文件
     */
	@Parameter(defaultValue = "true")
	public boolean generatingPom;
	
	/**
	 * 是否生成DTO
     */
	@Parameter(defaultValue = "true")
	private boolean generatingDto;
	
	/**
	 * 客户端项目groupId
     */
	@Parameter(defaultValue = "${project.groupId}")
	private String groupId;
	
	/**
	 * 客户端项目artifactId
     */
	@Parameter(defaultValue = "${project.artifactId}-client")
	private String artifactId;

	/**
	 * 客户端项目version
     */
	@Parameter(defaultValue = "${project.version}")
	private String version;
	
	/**
	 * 生成代码目标基础路径
     */
	@Parameter(defaultValue = "src/main/java")
	private String basePath;
	
	/**
	 * 生成DTO类目标包。generatingDto = true时才有效。defaultValue中的“-”符号会替换成“.”
     */
	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}.client.dto")
	private String dtoPkg;
	
	/**
	 * 生成service类目标包。defaultValue中的“-”符号会替换成“.”
     */
	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}.client.service")
	private String servicePkg;
	
	public void execute() throws MojoExecutionException {
		getLog().info("Generating Spring Cloud client...");
		
		// prepare
		dtoPkg = dtoPkg.replace('-', '.');
		servicePkg = servicePkg.replace('-', '.');
		
		// generate pom
		
		// scan Controller
		
		// generate DTOs
		
		// generate Services
		
		// generate Factory
		
		// generate Transformer
		
		getLog().info("Generated Spring Cloud client successfully.");
	}
	
}