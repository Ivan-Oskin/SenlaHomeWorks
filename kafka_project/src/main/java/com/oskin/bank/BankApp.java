package com.oskin.bank;

import com.oskin.bank.Producer.Producer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan(basePackages = {"com.oskin.bank"})
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.oskin.bank.repository")
@EnableScheduling
public class BankApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BankApp.class);
        Producer producer = context.getBean(Producer.class);
        producer.run();
    }
}
