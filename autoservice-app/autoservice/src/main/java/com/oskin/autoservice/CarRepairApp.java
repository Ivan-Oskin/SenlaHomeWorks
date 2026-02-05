package com.oskin.autoservice;
import com.oskin.autoservice.view.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarRepairApp {

    public static void main(String[] args) {
        SpringApplication.run(CarRepairApp.class, args);
    }

    @Bean
    public CommandLineRunner runApp(MainMenu mainMenu) {
        return args -> mainMenu.run();
    }
}
