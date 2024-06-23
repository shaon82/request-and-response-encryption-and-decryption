package com.example.demo;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<DecryptionFilter> decryptionFilter() {
        FilterRegistrationBean<DecryptionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DecryptionFilter());
        return registrationBean;
    }
}
