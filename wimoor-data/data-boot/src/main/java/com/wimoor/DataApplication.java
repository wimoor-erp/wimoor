package com.wimoor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.wimoor.data"})
public class DataApplication {

    public static void main(String[] args) {
       	ConfigurableApplicationContext context  =SpringApplication.run(DataApplication.class, args);
    }

}
