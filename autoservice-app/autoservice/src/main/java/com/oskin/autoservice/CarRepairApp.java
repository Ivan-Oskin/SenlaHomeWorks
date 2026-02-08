package com.oskin.autoservice;
import com.oskin.autoservice.view.MainMenu;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@ComponentScan(basePackages = {"com.oskin.autoservice", "com.oskin.config"})
@PropertySource("classpath:application.properties")
public class CarRepairApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CarRepairApp.class);
        MainMenu mainMenu = context.getBean(MainMenu.class);
        mainMenu.run();
        context.close();
    }
}
