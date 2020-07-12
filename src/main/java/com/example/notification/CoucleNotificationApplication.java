package com.example.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CoucleNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoucleNotificationApplication.class, args);
    }

}
