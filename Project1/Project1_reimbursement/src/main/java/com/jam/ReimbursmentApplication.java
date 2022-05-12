package com.jam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ReimbursmentApplication {

    public static void main(String args[]) {
        SpringApplication.run(ReimbursmentApplication.class, args);
    }
}
