package com.oskin.config;

import com.oskin.annotations.ConfigProperty;
import com.oskin.annotations.Singleton;

@Singleton
public class Config {
    @ConfigProperty(propertyName = "standard.file.csv.garage")
    private String standardFileCsvGarage;
    @ConfigProperty(propertyName = "standard.file.json.garage")
    private String standardFileJsonGarage;
    @ConfigProperty(propertyName = "standard.file.csv.master")
    private String standardFileCsvMaster;
    @ConfigProperty(propertyName = "standard.file.json.master")
    private String standardFileJsonMaster;
    @ConfigProperty(propertyName = "standard.file.csv.orders")
    private String standardFileCsvOrders;
    @ConfigProperty(propertyName = "standard.file.json.orders")
    private String standardFileJsonOrders;
    @ConfigProperty(propertyName = "rule.add.place", type = boolean.class)
    private boolean ruleAddPlace;
    @ConfigProperty(propertyName = "rule.delete.place", type = boolean.class)
    private boolean ruleDeletePlace;
    @ConfigProperty(propertyName = "rule.delete.order", type = boolean.class)
    private boolean ruleDeleteOrder;
    @ConfigProperty(propertyName = "rule.offset", type = boolean.class)
    private boolean ruleOffset;
    @ConfigProperty(propertyName = "standard.path.to.data")
    private String standardPathToData;
    @ConfigProperty(propertyName = "url.bd")
    private String urlBd;
    @ConfigProperty(propertyName = "user.bd")
    private String userBd;
    @ConfigProperty(propertyName = "password.bd")
    private String passwordBd;
    @ConfigProperty(propertyName = "standard.fiel.csv.order_master")
    private String standardFileCsvOrderMaster;

    public Config() {
        Configuration configuration = new Configuration();
        configuration.configure(this);
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

    public String getStandardFileJsonGarage() {
        return this.standardFileJsonGarage;
    }

    public String getStandardFileJsonMaster() {
        return this.standardFileJsonMaster;
    }

    public String getStandardFileJsonOrders() {
        return this.standardFileJsonOrders;
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

    public String getUrlBd() {
        return this.urlBd;
    }

    public String getUserBd() {
        return this.userBd;
    }

    public String getPasswordBd() {
        return this.passwordBd;
    }
}
