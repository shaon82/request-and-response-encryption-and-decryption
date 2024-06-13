package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class CustomInterceptor implements HandlerInterceptor {


    @Value("${spring.request.private.decryption-key}")
    public String privateKeyString;


    @Value("${spring.request.public.encryption-key}")
    public String publicKeyString;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("This is the preHandle method");

        String encryptedBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, String> result = new ObjectMapper().readValue(encryptedBody, HashMap.class);

        String decryptedData = EncryptDecryptService.decrypt(result.get("data"), privateKeyString);
        System.out.println("Decrypted Data: " + decryptedData);

        // Wrap the original request with the decrypted data
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request, decryptedData);

        // Set the wrapped request as an attribute
        request.setAttribute("wrappedRequest", wrappedRequest);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("this is post handle method");
        Map<String, String> map = new HashMap<>();
        if (response instanceof CustomHttpServletResponseWrapper) {
            CustomHttpServletResponseWrapper wrappedResponse = (CustomHttpServletResponseWrapper) response;
            String originalContent = wrappedResponse.getResponseContent();
            String s = EncryptDecryptService.encrypt(originalContent,publicKeyString);
            map.put("data",s);

            String json = new Gson().toJson(map);
            wrappedResponse.setResponseContent(json);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("this is after completion");


    }
}
