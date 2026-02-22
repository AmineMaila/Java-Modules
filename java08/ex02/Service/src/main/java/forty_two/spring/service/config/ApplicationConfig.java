package forty_two.spring.service.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:db.properies")
public class ApplicationConfig {
    @Value("${db.url}")
    private String dburl;

    @Value("${db.user}")
    private String dbuser;

    @Value("${db.password}")
    private String dbpassword;

    @Bean
    @Qualifier("driverManager")
    public DataSource driverManagerDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(dburl);
        ds.setUsername(dbuser);
        ds.setPassword(dbpassword);
        return ds;
    }

    @Bean
    @Qualifier("hikari")
    public DataSource hikariDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(dburl);
        ds.setUsername(dbuser);
        ds.setPassword(dbpassword);
        return ds;
    }
}
