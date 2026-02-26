package com.oskin.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("${standard.file.csv.garage}")
    private String standardFileCsvGarage;
    @Value("${standard.file.csv.master}")
    private String standardFileCsvMaster;
    @Value("${standard.file.csv.orders}")
    private String standardFileCsvOrders;
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
    @Value("${standard.file.csv.order_master}")
    private String standardFileCsvOrderMaster;
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


    public Config() {
    }

    public String getStandardFileCsvGarage() {
        return this.standardFileCsvGarage;
    }

    public String getStandardFileCsvMaster() {
        return this.standardFileCsvMaster;
    }

    public String getStandardFileCsvOrders() {
        return this.standardFileCsvOrders;
    }

    public String getStandardFileCsvOrderMaster() {
        return this.standardFileCsvOrderMaster;
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
}
