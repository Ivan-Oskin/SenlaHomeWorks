package com.oskin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://postgres:5438/carrepairdb");
        logger.info("!!!!!!!!!!!!!!! URL = " + dataSource.getUrl() + " !!!!!!!!!!!!!!!!!!!!!!!!1");
        dataSource.setUsername("carrepair_admin");
        dataSource.setPassword("Admin");
        return dataSource;
    }
}
