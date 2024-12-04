package com.eyinfo.webx.annotations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {"com.eyinfo.webx"}
)
public class WebMvcAutoConfiguration {
}
