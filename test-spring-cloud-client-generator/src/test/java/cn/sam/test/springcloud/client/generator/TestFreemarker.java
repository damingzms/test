package cn.sam.test.springcloud.client.generator;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import cn.sam.test.springcloud.client.generator.util.TemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarker {

	public static void main(String[] args) throws Exception {
		Configuration cfg = TemplateUtils.getFreemarkerConfiguration();
		Template template = cfg.getTemplate(TemplateUtils.TEMPLATE_NAME_POM);

		Map<String, Object> root = new HashMap<>();
		root.put("groupId", "test.groupId");
		root.put("artifactId", "test-artifactId");
		root.put("version", "1.0-SNAPSHOT");
		Writer out = new OutputStreamWriter(System.out);
		template.process(root, out);
	}

}
