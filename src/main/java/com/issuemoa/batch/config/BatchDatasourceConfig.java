package com.issuemoa.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.MariaDBDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@EnableJpaRepositories(
    basePackages = "com.issuemoa.batch.domain.batch", // Repository 패키지 경로
    entityManagerFactoryRef = "batchEntityManager",
    transactionManagerRef = "batchTransactionManager"
)
@Configuration
public class BatchDatasourceConfig {
    /*
    LocalContainerEntityManagerFactoryBean: SessionFactoryBean과 동일한 역활을 맡습니다.
        SessionFactoryBean과 동일하게 DataSource와 Hibernate Property, Entity가 위치한 package를 지정합니다.
        또한 Hibernate 기반으로 동작하는 것을 지정해하는 JpaVendor를 설정해줘야지 됩니다.
    HibernateJpaVendorAdapter: Hibernate vendor과 JPA간의 Adapter를 설정합니다.
     */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean batchEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(batchDataSource());
        em.setPackagesToScan(new String[] {"com.issuemoa.batch.domain.batch"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    // 스프링은 하나의 타입에 빈 객체가 여러개인경우 그 중 우선순위를 갖는 빈이라는것을 지정할 수 있도록 @Primary 어노테이션을 제공
    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager batchTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(batchEntityManager().getObject());
        return transactionManager;
    }
}