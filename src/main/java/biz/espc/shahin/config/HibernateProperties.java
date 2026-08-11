package biz.espc.shahin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * provided by ESPC software team
 * created on 1/28/2026 at 10:49 AM
 */
@Data
@Configuration("hibernateProperties")
public class HibernateProperties {
    @Value("${shahin.hibernate.dialect}")
    private String dialect;
    @Value("${shahin.hibernate.showSql}")
    private boolean showSql;
    @Value("${shahin.hibernate.hbm2ddl}")
    private String hbm2ddl;
}