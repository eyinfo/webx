package com.eyinfo.webx.annotations;

import com.eyinfo.webx.registrar.InjectionConfigurationRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(InjectionConfigurationRegistrar.class)
public @interface InjectionScan {
    Class<?>[] injectClasses() default {};
}
