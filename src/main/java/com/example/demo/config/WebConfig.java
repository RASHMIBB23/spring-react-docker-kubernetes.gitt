package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward requests to the main application entry point
        registry.addViewController("/").setViewName("forward:/build/index.html");
        registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/build/index.html");
        registry.addViewController("/{x:^(?!api$|swagger-ui.*$).*$}/**/{y:[\\w\\-]+}").setViewName("forward:/build/index.html");
    }
}
