package org.foo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.Invoker;

import java.util.concurrent.Callable;

import org.foo.rest.LoggerRest;
import org.foo.rest.MockerRest;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class RestTransformer implements ClassFileTransformer {

    private static final String MODE = "HT_MODE";

    /** The internal form class name of the class to transform */
    private String targetClassName;
    /** The class loader of the class we want to transform */
    private ClassLoader targetClassLoader;

    public RestTransformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        byte[] byteCode = classfileBuffer;
        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");

        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            LOGGER.info("Inside " + className + ".transform()");
            try {
                Class<?> clazz = Class.forName(className.replace('/', '.'));
                // var id =
                String mode = System.getenv("HT_MODE");
                if (mode == "RECORD") {
                    return new ByteBuddy()
                            .redefine(clazz)
                            .method(named("sayHello"))
                            .intercept(MethodDelegation.to(LoggerRest.class))
                            .make()
                            .load(clazz.getClassLoader(),
                                    ClassReloadingStrategy
                                            .fromInstalledAgent(ClassReloadingStrategy.Strategy.REDEFINITION))
                            .getBytes();
                }
                return new ByteBuddy()
                        .redefine(clazz)
                        .method(named("sayHello"))
                        .intercept(MethodDelegation.to(MockerRest.class))
                        .make()
                        .load(clazz.getClassLoader(),
                                ClassReloadingStrategy
                                        .fromInstalledAgent(ClassReloadingStrategy.Strategy.REDEFINITION))
                        .getBytes();

            } catch (Exception e) {
                LOGGER.error("BRO Exception" + e);
                e.printStackTrace();
            }
        }
        return byteCode;

    }

}
