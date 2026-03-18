package com.oskin.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Config {
    @Value("${rule.add.place}")
    private boolean ruleAddPlace;
    @Value("${rule.delete.place}")
    private boolean ruleDeletePlace;
    @Value("${rule.delete.order}")
    private boolean ruleDeleteOrder;
    @Value("${rule.offset}")
    private boolean ruleOffset;
    @Value("${standard.path.to.data}")
    private String standardPathToData;
    @Value("${db.driver}")
    private String driver;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${lb.changelog}")
    private String changelog;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private Long lifetime;


    public Config() {
    }
    public boolean getRuleAddPlace() {
        return this.ruleAddPlace;
    }

    public boolean getRuleDeletePlace() {
        return this.ruleDeletePlace;
    }

    public boolean getRuleDeleteOrder() {
        return this.ruleDeleteOrder;
    }

    public boolean getRuleOffset() {
        return this.ruleOffset;
    }

    public String getStandardPathToData() {
        return this.standardPathToData;
    }

    public String getDriver() {
        return this.driver;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getChangelog() {
        return this.changelog;
    }

    public Duration getLifetime() {
        return Duration.ofMillis(lifetime);
    }

    public String getSecret() {
        return this.secret;
    }
}
