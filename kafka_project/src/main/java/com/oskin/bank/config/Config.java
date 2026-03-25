package com.oskin.bank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${lb.changelog}")
    private String changelog;
    @Value("${db.driver}")
    private String driver;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getChangelog() {
        return changelog;
    }
}
