package biz.espc.shahin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * provided by ESPC software team
 * created on 1/28/2026 at 10:49 AM
 */

@Setter
@Getter
@Configuration("dataSourceProperties")
@DependsOn("hibernateProperties")
public class DataSourceProperties {
    @Value("${shahin.datasource.driver}")
    private String driver;
    @Value("${shahin.datasource.url}")
    private String url;
    @Value("${shahin.datasource.username}")
    private String username;
    @Value("${shahin.datasource.password}")
    private String password;
    @Value("${shahin.datasource.maximumPoolSize}")
    private Integer maximumPoolSize;
//    @Value("${shahin.datasource.cachePrepStmts}")
//    private Boolean cachePrepStmts;
    @Value("${shahin.datasource.prepStmtCacheSize}")
    private Integer prepStmtCacheSize;
    @Value("${shahin.datasource.prepStmtCacheSqlLimit}")
    private Integer prepStmtCacheSqlLimit;
    private Integer minimumIdle = 2;
    private long maxLifetime = 300000L;
    private long keepaliveTime = 120000L;
    private long idleTimeout = 60000L;
}