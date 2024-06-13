package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CustomInterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private CustomInterceptor customInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor);
    }

    @Bean
    public Filter customFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {}

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletRequest wrappedRequest = (HttpServletRequest) httpRequest.getAttribute("wrappedRequest");

                CustomHttpServletResponseWrapper wrappedResponse = new CustomHttpServletResponseWrapper((HttpServletResponse) response);
//                chain.doFilter(request, wrappedResponse);

                if (wrappedRequest != null) {
                    chain.doFilter(wrappedRequest, wrappedResponse);
                } else {
                    chain.doFilter(request, wrappedResponse);
                }
            }

            @Override
            public void destroy() {}
        };
    }
}
