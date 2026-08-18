package com.healthcare.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@EnableCaching
@SpringBootApplication(excludeName = {
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration",
    "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@EnableAsync
public class ClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public org.springframework.cache.CacheManager cacheManager() {
		return new org.springframework.cache.concurrent.ConcurrentMapCacheManager();
	}
}
