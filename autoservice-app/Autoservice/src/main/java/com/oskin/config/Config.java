package com.oskin.config;
import com.oskin.Annotations.ConfigProperty;
import com.oskin.Annotations.Singleton;

@Singleton
public class Config {
    @ConfigProperty(propertyName = "standart.file.csv.garage")
    private String standartFileCsvGarage;
    @ConfigProperty(propertyName = "standart.file.json.garage")
    private String standartFileJsonGarage;
    @ConfigProperty(propertyName = "standart.file.csv.master")
    private String standartFileCsvMaster;
    @ConfigProperty(propertyName = "standart.file.json.master")
    private String standartFileJsonMaster;
    @ConfigProperty(propertyName = "standart.file.csv.orders")
    private String standartFileCsvOrders;
    @ConfigProperty(propertyName = "standart.file.json.orders")
    private String standartFileJsonOrders;
    @ConfigProperty(propertyName = "rule.add.place",type = boolean.class)
    boolean ruleAddPlace;
    @ConfigProperty( propertyName = "rule.delete.place",type = boolean.class)
    boolean ruleDeletePlace;
    @ConfigProperty(propertyName = "rule.delete.order",type = boolean.class)
    boolean ruleDeleteOrder;
    @ConfigProperty(propertyName = "rule.offset",type = boolean.class)
    boolean ruleOffset;
    @ConfigProperty(propertyName = "standart.path.to.data")
    String standartPathToData;
    @ConfigProperty(propertyName = "url.bd")
    String urlBd;
    @ConfigProperty(propertyName = "user.bd")
    String userBd;
    @ConfigProperty(propertyName = "password.bd")
    String passwordBd;

    public Config(){
        Configuration configuration = new Configuration();
        configuration.configure(this);
    }

    public String getStandartFileCsvGarage(){
        return this.standartFileCsvGarage;
    }
    public String getStandartFileCsvMaster(){
        return this.standartFileCsvMaster;
    }
    public String getStandartFileCsvOrders(){
        return this.standartFileCsvOrders;
    }
    public String getStandartFileJsonGarage(){
        return this.standartFileJsonGarage;
    }
    public String getStandartFileJsonMaster(){
        return this.standartFileJsonMaster;
    }
    public String getStandartFileJsonOrders(){
        return this.standartFileJsonOrders;
    }
    public boolean getRuleAddPlace(){
        return this.ruleAddPlace;
    }
    public boolean getRuleDeletePlace(){
        return this.ruleDeletePlace;
    }
    public boolean getRuleDeleteOrder(){
        return this.ruleDeleteOrder;
    }
    public boolean getRuleOffset(){
        return this.ruleOffset;
    }
    public String getStandartPathToData(){return this.standartPathToData;}
    public String getUrlBd(){return this.urlBd;}
    public String getUserBd(){return this.userBd;}
    public String getPasswordBd(){return this.passwordBd;}
}
