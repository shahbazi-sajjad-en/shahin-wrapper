package biz.espc.shahin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                ReactiveSecurityAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                SecurityAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
        , scanBasePackages = {
        "biz.espc.shahin"
}
)
@EnableTransactionManagement
@EnableCaching
@ServletComponentScan
public class ShahinWrapperApplication {

    private static final Logger logger = LoggerFactory.getLogger(ShahinWrapperApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShahinWrapperApplication.class, args);

        logger.info("Application started successfully!");

    }

}