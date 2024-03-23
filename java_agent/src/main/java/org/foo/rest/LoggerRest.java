package org.foo.rest;

import org.foo.LOGGER;

public class LoggerRest {
    public static String sayHello(String world) {
        String value = "Hello " + world + "!";
        LOGGER.info("sayHello returned: " + value);
        return value;
    }
}
