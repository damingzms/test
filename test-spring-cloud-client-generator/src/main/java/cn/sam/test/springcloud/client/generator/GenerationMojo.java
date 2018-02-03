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
	 * 生成DTO类目标包
     */
	@Parameter(defaultValue = "${project.groupId}.client.dto")
	private String dtoPkg;
	
	/**
	 * 生成service类目标包
     */
	@Parameter(defaultValue = "${project.groupId}.client.service")
	private String servicePkg;
	
	public void execute() throws MojoExecutionException {
		getLog().info("Generating Spring Cloud client...");
		getLog().warn(generatingPom + "");
		getLog().warn(generatingDto + "");
		getLog().warn(groupId);
		getLog().warn(artifactId);
		getLog().warn(version);
		getLog().warn(basePath);
		getLog().warn(servicePkg);
		
		// pom.xml
		
		// ServiceFactory.java，将移到独立jar，不再动态生成
		
		// Transformer.java，将移到独立jar，不再动态生成
		
		// DTO
		
		// Service
		
		getLog().info("Generated Spring Cloud client successfully.");
	}
	
}