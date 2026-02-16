package io.kamailio.admin.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${DB_TYPE:mysql}")
    private String dbType;

    @Value("${DB_HOST:localhost}")
    private String host;

    @Value("${DB_PORT:3306}")
    private int port;

    @Value("${DB_USERNAME:kamailio}")
    private String username;

    @Value("${DB_PASSWORD:kamailiorw}")
    private String password;

    @Value("${DB_DATABASE:kamailio}")
    private String database;

    @Bean
    @Primary
    public DataSource dataSource() {
        var ds = new HikariDataSource();
        boolean isPostgres = "postgres".equalsIgnoreCase(dbType) || "postgresql".equalsIgnoreCase(dbType);
        if (isPostgres) {
            ds.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
            ds.setDriverClassName("org.postgresql.Driver");
        } else {
            ds.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaximumPoolSize(10);
        return ds;
    }
}
