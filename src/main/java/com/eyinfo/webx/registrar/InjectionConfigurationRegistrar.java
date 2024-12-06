package com.eyinfo.webx.registrar;

import com.eyinfo.webx.annotations.*;
import com.eyinfo.webx.listener.AuthenticationVerifyInterceptor;
import com.eyinfo.webx.listener.InitAwareInterceptor;
import com.eyinfo.webx.utils.InjectionUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Constructor;
import java.util.List;

public class InjectionConfigurationRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(InjectionScan.class.getName()));
        Class<?>[] injectClasses = attributes.getClassArray("injectClasses");
        for (Class<?> injectClass : injectClasses) {
            if (injectClass.isAnnotationPresent(ConfigurationInterceptor.class)) {
                loadInterceptors(injectClass);
            } else if (injectClass.isAnnotationPresent(ConfigurationApplicationRunner.class)) {
                loadRunners(injectClass);
            } else if (injectClass.isAnnotationPresent(AuthenticationVerify.class)) {
                loadAuthenticationVerify(injectClass);
            } else if (injectClass.isAnnotationPresent(ConfigurationAware.class)) {
                loadApplicationContextInitAction(injectClass);
            }
        }
    }

    private void loadApplicationContextInitAction(Class<?> injectClass) {
        try {
            Constructor<?> constructor = injectClass.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            if (instance instanceof InitAwareInterceptor) {
                InjectionUtils.getInitAwareInterceptors().add((InitAwareInterceptor) instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAuthenticationVerify(Class<?> injectClass) {
        try {
            Constructor<?> constructor = injectClass.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            if (instance instanceof AuthenticationVerifyInterceptor) {
                InjectionUtils.getAuthenticationVerifyInterceptors().add((AuthenticationVerifyInterceptor) instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRunners(Class<?> injectClass) {
        List<Runnable> applicationRunners = InjectionUtils.getApplicationRunners();
        try {
            Constructor<?> constructor = injectClass.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            if (instance instanceof Runnable) {
                applicationRunners.add((Runnable) instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInterceptors(Class<?> injectClass) {
        List<HandlerInterceptor> interceptors = InjectionUtils.getInterceptors();
        try {
            Constructor<?> constructor = injectClass.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            if (instance instanceof HandlerInterceptor) {
                interceptors.add((HandlerInterceptor) instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
