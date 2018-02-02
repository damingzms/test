package cn.sam.test.springcloud.client.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @goal generate
 */
public class GenMojo extends AbstractMojo {
	
	/**
	 * 生成代码目标路径
     * @parameter
     */
	private String basePath;
	
	/**
	 * 生成代码目标包
	 * 如果copy=true，此配置不生效
     * @parameter
     */
	private String pkg;

	/**
	 * 是否复制现有的service和dto
     * @parameter
     */
	private boolean copy;
	
	/**
	 * 复制源文件所在路径
     * @parameter
     */
	private String sourceBasePath;
	
	/**
	 * 复制源文件所在包
     * @parameter
     */
	private String sourcePkg;
	
	public void execute() throws MojoExecutionException {
		getLog().info("Generating Spring Cloud client...");
		getLog().warn(basePath);
		
		getLog().info("Generated Spring Cloud client successfully.");
	}
	
}