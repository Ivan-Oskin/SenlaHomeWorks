package com.oskin.bank;

import com.oskin.bank.service.Consumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(basePackages = {"com.oskin.bank"})
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.oskin.bank.repository")
public class ConsumerApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerApp.class);
        Consumer consumer = context.getBean(Consumer.class);
        consumer.run();
    }
}
