package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        // System.out.println("\n\nAPP Starts\n=============");
        // System.out.println("MODE:" + System.getenv("HT_MODE"));
        // Rest r = new Rest();
        // System.out.printf(r.sayHello("bomb"));

        SpringApplication.run(Application.class, args);

    }
}