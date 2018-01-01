package cn.sam.test.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * http://maven.apache.org/guides/plugin/guide-java-plugin-development.html
 * http://maven.apache.org/guides/mini/guide-configuring-plugins.html
 * 
 * 1.run a standalone goal
 *   mvn cn.sam.test:test-maven-plugin:0.0.1-SNAPSHOT:sayhi
 *   
 *   There are several ways to reduce the amount of required typing:
		a.If you need to run the latest version of a plugin installed in your local repository, you can omit its version number. So just use
		  mvn cn.sam.test:test-maven-plugin:sayhi
		b.You can assign a shortened prefix to your plugin, such as "mvn test:sayhi". This is done automatically if you follow the convention of using ${prefix}-maven-plugin (or maven-${prefix}-plugin if the plugin is part of the Apache Maven project). You may also assign one through additional configuration.
		c.Finally, you can also add your plugin's groupId to the list of groupIds searched by default. To do this, you need to add the following to your ${user.home}/.m2/settings.xml file:
			<pluginGroups>
			  <pluginGroup>sample.plugin</pluginGroup>
			</pluginGroups>
		  At this point, you can run the mojo with "mvn test:sayhi".
 *
 *2.You can also configure your plugin to attach specific goals to a particular phase of the build lifecycle. Here is an example:
	  <build>
	    <plugins>
	      <plugin>
	        <groupId>cn.sam.test</groupId>
	        <artifactId>test-maven-plugin</artifactId>
	        <version>0.0.1-SNAPSHOT</version>
	        <executions>
	          <execution>
	            <phase>compile</phase>
	            <goals>
	              <goal>sayhi</goal>
	            </goals>
	          </execution>
	        </executions>
	      </plugin>
	    </plugins>
	  </build>
    This causes the simple mojo to be executed whenever Java code is compiled. 
    
 * @goal sayhi
 */
//@Mojo(name = "sayhi")
public class GreetingMojo extends AbstractMojo {

	// 类和属性需用同样的注解方式
	@Parameter
    private String greeting;
	
	/**
     * @parameter
     */
	private String userName;
	
	public void execute() throws MojoExecutionException {
		getLog().info(greeting + " " + userName);
	}
	
}