package com.surpassun.cash.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.surpassun.cash.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {

//    @Bean
//    @ConfigurationProperties(prefix="spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
    
/**
    @Bean
    public DataSource dataSource() {
        log.debug("Configuring Datasource");
        if (propertyResolver.getProperty("url") == null && propertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    "cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(environment.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        //config.setDriverClassName(propertyResolver.getProperty("driverClassName"));
        config.setDataSourceClassName(propertyResolver.getProperty("dataSourceClassName"));
        //config.setJdbcUrl(propertyResolver.getProperty("jdbcUrl"));
        if (propertyResolver.getProperty("url") == null || "".equals(propertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName", propertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName", propertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", propertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", propertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", propertyResolver.getProperty("password"));

        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(propertyResolver.getProperty("dataSourceClassName"))) {
            config.addDataSourceProperty("cachePrepStmts", propertyResolver.getProperty("cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize", propertyResolver.getProperty("prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", propertyResolver.getProperty("prepStmtCacheSqlLimit", "2048"));
            config.addDataSourceProperty("useServerPrepStmts", propertyResolver.getProperty("useServerPrepStmts", "true"));
        }
        return new HikariDataSource(config);
    }
    @Bean(name = {"org.springframework.boot.autoconfigure.AutoConfigurationUtils.basePackages"})
    public List<String> getBasePackages() {
        List<String> basePackages = new ArrayList<>();
        basePackages.add("com.surpassun.cash.domain");
        return basePackages;
    }
 **/
}

