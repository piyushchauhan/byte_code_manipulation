package org.foo;

public class LOGGER {
    public static void info(String message) {
        System.out.println("[Agent] [INFO] " + message);
    }

    public static void error(String string) {
        System.out.println("[Agent] [ERROR] " + string);
    }
}
