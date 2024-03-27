package org.foo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ImplementationDefinition;
import net.bytebuddy.dynamic.DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
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
                String mode = System.getenv("HT_MODE");
                ImplementationDefinition method = new ByteBuddy().redefine(clazz).method(named("sayHello"));
                ReceiverTypeDefinition methodDefinition;
                // if (mode == "RECORD") {
                    methodDefinition = method.intercept(MethodDelegation.to(LoggerRest.class));
                // } else {
                //     methodDefinition = method.intercept(MethodDelegation.to(MockerRest.class));
                // }
                return methodDefinition
                        .make()
                        .load(clazz.getClassLoader(),
                                ClassReloadingStrategy.fromInstalledAgent(ClassReloadingStrategy.Strategy.REDEFINITION))
                        .getBytes();

            } catch (Exception e) {
                LOGGER.error("BRO Exception" + e);
                e.printStackTrace();
            }
        }
        return byteCode;
    }
}
