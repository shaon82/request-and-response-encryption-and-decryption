package com.example.demo;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, String> map = new HashMap<>();
        if (response instanceof CustomHttpServletResponseWrapper) {
            CustomHttpServletResponseWrapper wrappedResponse = (CustomHttpServletResponseWrapper) response;
            String originalContent = wrappedResponse.getResponseContent();
            String s = EncryptDecryptService.encrypt(originalContent,EncryptionKey.PUBLIC_KEY);
            map.put("data",s);
            String json = new Gson().toJson(map);
            wrappedResponse.setResponseContent(json);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
