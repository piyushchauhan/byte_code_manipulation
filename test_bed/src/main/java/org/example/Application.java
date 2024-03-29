package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println("\n\nAPP Starts\n=============");
        // Log all environment variables
        System.getenv().forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println("MODE:" + System.getProperty("HT_MODE"));
        Rest r = new Rest();
        System.out.printf(r.sayHello("World!"));
        r = null;

        SpringApplication.run(Application.class, args);
    }
}