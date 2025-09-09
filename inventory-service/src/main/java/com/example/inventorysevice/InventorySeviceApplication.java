package com.example.inventorysevice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class InventorySeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventorySeviceApplication.class, args);
    }

}
