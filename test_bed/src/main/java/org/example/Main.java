package org.example;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n\nAPP Starts\n=============");
        System.out.println("MODE:" + System.getenv("HT_MODE"));
        Rest r = new Rest();
        System.out.printf(r.sayHello("bomb"));
    }
}