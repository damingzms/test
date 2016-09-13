package cn.sam.redis.redisson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

public class RedissonIntegrationTest {
	
	public static void main(String[] args) {
		try {
			testSpringCache();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedissonUtil.shutdown();
		}
	}
	
/* 1.Spring cache Start */

	/**
	 * Spring cache <br>
	 * Redisson provides integration between Spring framework and Redis. It fully implements Spring Cache Abstraction. 
	 * Each Cache instance has two important parameters: ttl and maxIdleTime and stores data infinitely if they not defined or equals to 0.
	 */
	public static void testSpringCache() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//		ctx.register(Application.class);
		ctx.register(Application1.class);
		ctx.refresh();
		CacheManager cm = (CacheManager) ctx.getBean("cacheManager");
		Cache cache = cm.getCache("testMap");
		cache.put("test1", "bar");
		String val = cache.get("test1", String.class);
		System.out.println("value = " + val);
		ctx.close();
	}
	
	@Configuration
	@ComponentScan
	@EnableCaching
	public static class Application {

		@Bean(destroyMethod = "shutdown")
		RedissonClient redisson() throws IOException {
			Config config = new Config();
//			config.useClusterServers().addNodeAddress("127.0.0.1:7004", "127.0.0.1:7001");
			config.useSingleServer().setAddress("localhost:6379");
			return Redisson.create(config);
		}

		@Bean
		CacheManager cacheManager(RedissonClient redissonClient) {
			Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
			// create "testMap" cache with ttl = 24 seconds and maxIdleTime = 12
			// seconds
			config.put("testMap", new CacheConfig(24 * 60 * 1000, 12 * 60 * 1000));
			return new RedissonSpringCacheManager(redissonClient, config);
		}

	}
	
	//Cache configuration could be read from JSON or YAML configuration files:
	@Configuration
	@ComponentScan
	@EnableCaching
	public static class Application1 {

		@Bean(destroyMethod = "shutdown")
		RedissonClient redisson(@Value("classpath:/cn/sam/redis/redisson/redisson.json") Resource configFile) throws IOException {
			Config config = Config.fromJSON(configFile.getInputStream());
			return Redisson.create(config);
		}

		@Bean
		CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
			return new RedissonSpringCacheManager(redissonClient, "classpath:/cn/sam/redis/redisson/cache-config.json");
		}

	}
	
/* 1.Spring cache End */
	
}
