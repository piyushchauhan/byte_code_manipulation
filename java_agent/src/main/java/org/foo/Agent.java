package org.foo;

import static net.bytebuddy.matcher.ElementMatchers.named;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import java.util.concurrent.Callable;

public class Agent {
    public static void premain(String args, Instrumentation inst) {
        ByteBuddyAgent.install();
        LOGGER.info("In premain method");
        LOGGER.info("Mode:" + System.getenv("HT_MODE"));

        String className = "org.example.Rest";
        transformClass(className, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        ByteBuddyAgent.install();
        LOGGER.info("In agentmain method");
        LOGGER.info("Mode:" + System.getenv("HT_MODE"));
        String className = "org.example.Rest";
        transformClass(className, inst);
    }

    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Class [{}] not found with Class.forName");
        }
        LOGGER.info("Class [{}] not found with Class.forName, trying getAllLoadedClasses");
        // otherwise iterate all loaded classes and find what we want
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        LOGGER.info("Transforming class: [" + clazz.getName() + "]");
        RestTransformer dt = new RestTransformer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Transform failed for: [" + clazz.getName() + "]", ex);
        }
    }
}