package biz.espc.shahin.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * provided by ESPC software team
 * created on 1/28/2026 at 10:49 AM
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "biz.espc.shahin.repository",
        entityManagerFactoryRef = "shahinEntityManager",
        transactionManagerRef = "shahinTransactionManager"
  )
public class PersistenceConfiguration {

    private final DataSourceProperties dataSourceProperties;
    private final HibernateProperties hibernateProperties;

    public PersistenceConfiguration(DataSourceProperties dataSource, HibernateProperties hibernate) {
        this.dataSourceProperties = dataSource;
        this.hibernateProperties = hibernate;
    }

    @Bean(name = "shahinDataSource")
    @DependsOn("dataSourceProperties")
    public HikariDataSource shahinDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        config.setDriverClassName(dataSourceProperties.getDriver());
        config.setMinimumIdle(dataSourceProperties.getMinimumIdle());
        config.setMaxLifetime(dataSourceProperties.getMaxLifetime());
        config.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        config.setKeepaliveTime(dataSourceProperties.getKeepaliveTime());
        config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
//        config.addDataSourceProperty("cachePrepStmts", dataSourceProperties.getCachePrepStmts());
        config.addDataSourceProperty("prepStmtCacheSize", dataSourceProperties.getPrepStmtCacheSize());
        config.addDataSourceProperty("prepStmtCacheSqlLimit", dataSourceProperties.getPrepStmtCacheSqlLimit());
        return new HikariDataSource(config);
    }

    @Bean(name = "shahinEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(getPackagesToScan());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        final Map<String, Object> properties = getStringObjectMap();
        em.setJpaPropertyMap(properties);
        return em;
    }

    private @NonNull Map<String, Object> getStringObjectMap() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", hibernateProperties.getDialect());
        properties.put("hibernate.show_sql", hibernateProperties.isShowSql());
        properties.put("hibernate.hbm2ddl.auto", hibernateProperties.getHbm2ddl());
        properties.put("hibernate.generate_statistics", Boolean.FALSE);
        properties.put("hibernate.jdbc.use_get_generated_keys", Boolean.TRUE);
        properties.put("hibernate.physical_naming_strategy", ShahinPhysicalNamingStrategy.class.getName());
        return properties;
    }

    @Bean(name = "shahinTransactionManager")
    protected PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }

    public String[] getPackagesToScan() {
        return new String[]{"biz.espc.shahin.entity", "biz.espc.shahin.entity.*"};
    }

}