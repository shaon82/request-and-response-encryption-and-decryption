package com.example.demo;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> decryptedParams;
    private final String decryptedData;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, Map<String, String[]> decryptedParams) {
        super(request);
        this.decryptedParams = decryptedParams;
        this.decryptedData = null;
    }

    public CustomHttpServletRequestWrapper(HttpServletRequest request, String decryptedData) {
        super(request);
        this.decryptedData = decryptedData;
        this.decryptedParams = null;
    }

    @Override
    public String getParameter(String name) {
        if (decryptedParams != null) {
            String[] values = decryptedParams.get(name);
            return (values != null && values.length > 0) ? values[0] : null;
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (decryptedParams != null) {
            return decryptedParams;
        }
        return super.getParameterMap();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (decryptedParams != null) {
            return decryptedParams.get(name);
        }
        return super.getParameterValues(name);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (decryptedData != null) {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
        return super.getReader();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (decryptedData != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedData.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }
        return super.getInputStream();
    }


}
