package cn.sam.test.springcloud.client.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import cn.sam.test.springcloud.client.generator.util.FileUtils;
import cn.sam.test.springcloud.client.generator.util.TemplateUtils;
import freemarker.template.Template;

@Mojo(name = "generate")
public class GenerationMojo extends AbstractMojo {

	/**
	 * 当前项目根目录
	 */
	@Parameter(defaultValue = "${project.basedir}")
	private String currentProjectBaseDir;
	
	/**
	 * 扫描此包及其子包下面的Controller和DTO。多个包以“,”分开
	 */
	@Parameter(defaultValue = "${project.groupId}")
	private String packagesToScan;
	
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
	 * 生成的项目根目录。默认值：与当前项目根目录同级的名为artifactId参数值的目录
	 */
	@Parameter
	private String baseDir;
	
	/**
	 * src目录
     */
	@Parameter(defaultValue = "/src/main/java")
	private String srcDir;
	
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
	
	private File dtoDir;
	
	private File serviceDir;
	
	public void execute() throws MojoExecutionException {
		Log log = getLog();
		log.info("Generating Spring Cloud client project.");
		
		// prepare
		if (baseDir == null) {
			baseDir = new File(currentProjectBaseDir, ".." + File.separator + artifactId).getPath();
		}
		dtoPkg = dtoPkg.replace('-', '.');
		servicePkg = servicePkg.replace('-', '.');
		
		try {
			
			// generate Project Structure
			genProjectStructure(log);
			
			// generate POM
			genPom(log);
			
			// scan
			
			// generate DTOs
			
			// generate Services
			
			// generate Factory
			
			// generate Transformer
			
		} catch (Throwable e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	private void genProjectStructure(Log log) throws Exception {
		File file = FileUtils.mkdirs(null, baseDir, srcDir);
		dtoDir = FileUtils.mkdirs(file, dtoPkg.replace('.', File.separatorChar));
		serviceDir = FileUtils.mkdirs(file, servicePkg.replace('.', File.separatorChar));
	}
	
	private void genPom(Log log) throws Exception {
		Template template = TemplateUtils.getFreemarkerConfiguration().getTemplate(TemplateUtils.TEMPLATE_NAME_POM);
		Map<String, Object> root = new HashMap<>();
		root.put("groupId", groupId);
		root.put("artifactId", artifactId);
		root.put("version", version);
		FileOutputStream fos = null;
		try {
			fos = FileUtils.getFileOutputStream(log, null, baseDir, TemplateUtils.templateName2FileName(TemplateUtils.TEMPLATE_NAME_POM));
			Writer writer = new OutputStreamWriter(fos);
			template.process(root, writer);
		} finally {
			FileUtils.flushAndClose(fos, log);
		}
	}
	
}