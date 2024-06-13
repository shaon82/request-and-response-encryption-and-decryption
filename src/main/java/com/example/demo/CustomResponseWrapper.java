package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class CustomResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
    private ServletOutputStream servletOutputStream = new ServletOutputStream() {
        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {}

        @Override
        public void write(int b) throws IOException {
            byteArrayOutputStream.write(b);
        }
    };

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return servletOutputStream;
    }

    public String getCapturedResponse() {
        printWriter.flush();
        return byteArrayOutputStream.toString();
    }
}


