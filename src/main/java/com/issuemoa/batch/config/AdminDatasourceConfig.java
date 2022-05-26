package com.issuemoa.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableJpaRepositories(
    basePackages = "com.issuemoa.batch.domain", // Repository 패키지 경로
    entityManagerFactoryRef = "adminEntityManager",
    transactionManagerRef = "adminTransactionManager"
)
@Configuration
public class AdminDatasourceConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean adminEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(adminDataSource());
        em.setPackagesToScan(new String[] {"com.issuemoa.batch.domain"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.admin-datasource")
    public DataSource adminDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public PlatformTransactionManager adminTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(adminEntityManager().getObject());
        return transactionManager;
    }
}