package cn.sam.test.springcloud.client.generator.util;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

public final class TemplateUtils {
	
	public static final String TEMPLATE_NAME_POSTFIX = ".ftl";
	
	public static final String TEMPLATE_NAME_POM = "pom.xml" + TEMPLATE_NAME_POSTFIX;
	
	private static final String TEMPLATE_DIRECTORY = "/templates";
	
	private static final Configuration FREEMARKER_CFG = new Configuration(Configuration.VERSION_2_3_27);

	static {
		try {
			FREEMARKER_CFG.setClassLoaderForTemplateLoading(TemplateUtils.class.getClassLoader(), TEMPLATE_DIRECTORY);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		FREEMARKER_CFG.setDefaultEncoding("UTF-8");
		FREEMARKER_CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}
	
	public static Configuration getFreemarkerConfiguration() {
		return FREEMARKER_CFG;
	}
	
	public static String templateName2FileName(String templateName) {
		int endIndex = templateName.lastIndexOf(TEMPLATE_NAME_POSTFIX);
		return templateName.substring(0, endIndex);
	}
	
}
