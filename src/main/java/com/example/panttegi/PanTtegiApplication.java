package com.example.panttegi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PanTtegiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PanTtegiApplication.class, args);
    }

}
