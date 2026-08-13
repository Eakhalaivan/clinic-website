package com.healthcare.clinic.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.healthcare.clinic",
        excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.REGEX,
                pattern = "com\\.healthcare\\.clinic\\.pharmacy\\..*"
        ),
        entityManagerFactoryRef = "clinicEntityManagerFactory",
        transactionManagerRef = "clinicTransactionManager"
)
public class ClinicDatabaseConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "clinicDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.clinic")
    public DataSource clinicDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "clinicEntityManagerFactory")
    @org.springframework.context.annotation.DependsOn("clinicFlyway")
    public LocalContainerEntityManagerFactoryBean clinicEntityManagerFactory(
            @Qualifier("clinicDataSource") DataSource dataSource,
            org.springframework.core.env.Environment env) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPersistenceUnitName("clinic");
        em.setPackagesToScan(
                "com.healthcare.clinic.identity",
                "com.healthcare.clinic.patient",
                "com.healthcare.clinic.doctor",
                "com.healthcare.clinic.nursing",
                "com.healthcare.clinic.reception",
                "com.healthcare.clinic.appointment",
                "com.healthcare.clinic.medicalrecord",
                "com.healthcare.clinic.laboratory",
                "com.healthcare.clinic.billing",
                "com.healthcare.clinic.insurance",
                "com.healthcare.clinic.hr",
                "com.healthcare.clinic.finance",
                "com.healthcare.clinic.branch",
                "com.healthcare.clinic.superadmin",
                "com.healthcare.clinic.marketing",
                "com.healthcare.clinic.ecommerce",
                "com.healthcare.clinic.support",
                "com.healthcare.clinic.vendor",
                "com.healthcare.clinic.ambulance",
                "com.healthcare.clinic.radiology",
                "com.healthcare.clinic.analytics",
                "com.healthcare.clinic.notification",
                "com.healthcare.clinic.clinicaldecision",
                "com.healthcare.clinic.backoffice",
                "com.healthcare.clinic.inventory",
                "com.healthcare.clinic.tenant",
                "com.healthcare.clinic.subscription",
                "com.healthcare.clinic.ai",
                "com.healthcare.clinic.fhir"
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        String dialect = env.getProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
        String ddlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto", "validate");
        if (dialect.contains("H2")) {
            ddlAuto = "update";
        }
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean(name = "clinicTransactionManager")
    public PlatformTransactionManager clinicTransactionManager(
            @Qualifier("clinicEntityManagerFactory") LocalContainerEntityManagerFactoryBean clinicEntityManagerFactory) {
        return new JpaTransactionManager(clinicEntityManagerFactory.getObject());
    }
}
