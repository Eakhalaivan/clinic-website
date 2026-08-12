package com.healthcare.clinic.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.healthcare.clinic.pharmacy",
        entityManagerFactoryRef = "pharmacyEntityManagerFactory",
        transactionManagerRef = "pharmacyTransactionManager"
)
public class PharmacyDatabaseConfig {

    @Bean(name = "pharmacyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.pharmacy")
    public DataSource pharmacyDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pharmacyEntityManagerFactory")
    @org.springframework.context.annotation.DependsOn("pharmacyFlyway")
    public LocalContainerEntityManagerFactoryBean pharmacyEntityManagerFactory(
            @Qualifier("pharmacyDataSource") DataSource dataSource,
            org.springframework.core.env.Environment env) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPersistenceUnitName("pharmacy");
        em.setPackagesToScan(
                "com.healthcare.clinic.pharmacy"
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "validate"));
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "pharmacyTransactionManager")
    public PlatformTransactionManager pharmacyTransactionManager(
            @Qualifier("pharmacyEntityManagerFactory") LocalContainerEntityManagerFactoryBean pharmacyEntityManagerFactory) {
        return new JpaTransactionManager(pharmacyEntityManagerFactory.getObject());
    }
}
