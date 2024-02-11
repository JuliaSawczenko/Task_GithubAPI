package com.task_githubapi.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

    @Component
    @Order(1)
    public class AcceptHeaderFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String acceptHeader = httpRequest.getHeader("Accept");

            if (acceptHeader != null && !acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                httpResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpResponse.getWriter().write("{\"error\": \"This API only supports application/json.\"}");
                return;
            }

            chain.doFilter(request, response);
        }
    }
