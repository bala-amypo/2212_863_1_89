package com.example.demo.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.servlet.HelloServlet;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<HelloServlet> helloServletBean() {
        ServletRegistrationBean<HelloServlet> bean = new ServletRegistrationBean<>(
            new HelloServlet(), "/hello-servlet");
        bean.setLoadOnStartup(1);
        return bean;
    }
}