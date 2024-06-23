package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Order(1)
public class DecryptionFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            if ("GET".equalsIgnoreCase(httpRequest.getMethod())) {
                decryptQueryParameters(httpRequest, chain, response);
            } else if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
                decryptRequestBody(httpRequest, chain, response);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void decryptQueryParameters(HttpServletRequest request, FilterChain chain, ServletResponse response)
            throws IOException, ServletException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        Map<String, String[]> decryptedParams = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] encryptedValues = entry.getValue();
            String[] decryptedValues = new String[encryptedValues.length];

            try {
                for (int i = 0; i < encryptedValues.length; i++) {
                    decryptedValues[i] = EncryptDecryptService.decrypt(encryptedValues[i], EncryptionKey.PRIVATE_KEY);
                }
            } catch (Exception e) {
                throw new ServletException("Decryption error", e);
            }
            decryptedParams.put(key, decryptedValues);
        }

        HttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request, decryptedParams);
        chain.doFilter(wrappedRequest, response);
    }

    private void decryptRequestBody(HttpServletRequest request, FilterChain chain, ServletResponse response)
            throws IOException, ServletException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String encryptedData = sb.toString();
        try {
        Map<String, String> result = new ObjectMapper().readValue(encryptedData, HashMap.class);

            String decryptedData = EncryptDecryptService.decrypt(result.get("data"), EncryptionKey.PRIVATE_KEY);
            HttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(request, decryptedData);
            chain.doFilter(wrappedRequest, response);
        } catch (Exception e) {
            throw new ServletException("Decryption error", e);
        }
    }


}