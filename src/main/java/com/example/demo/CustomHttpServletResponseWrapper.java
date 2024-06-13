package com.example.demo;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintWriter writer = new PrintWriter(outputStream);

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public String getResponseContent() throws IOException {
        writer.flush();
        return outputStream.toString();
    }

    public void setResponseContent(String content) throws IOException {
        super.getOutputStream().write(content.getBytes());
    }
}

