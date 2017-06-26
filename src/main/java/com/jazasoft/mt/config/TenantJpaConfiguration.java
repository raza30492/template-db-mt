package com.jazasoft.mt.config;


import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Configuration
@ComponentScan("com.jazasoft.mt.tenant")
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManager",
        transactionManagerRef = "tenantTransactionManager",
        basePackages = "com.jazasoft.mt.repository.tenant"
)
@EnableTransactionManagement
public class TenantJpaConfiguration {

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Primary
    @Bean(name = "tenantEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       MultiTenantConnectionProvider connectionProvider,
                                                                       CurrentTenantIdentifierResolver tenantResolver) {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan("com.jazasoft.mt.entity.tenant");
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);

        properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        emfBean.setJpaProperties(additionalJpaProperties(dataSource));
        emfBean.setJpaPropertyMap(properties);
        return emfBean;
    }

    @Primary
    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory tenantEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }

    @Autowired
    private Properties additionalJpaProperties(DataSource dataSource) {
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : jpaProperties.getHibernateProperties(dataSource).entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        return properties;
    }
}