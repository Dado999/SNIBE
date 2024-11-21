package com.example.config;

import com.example.controllers.AuthController;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WafFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(WafFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUrl = httpRequest.getRequestURL().toString();
        logger.info("Incoming request: {}", requestUrl);
        String query = httpRequest.getQueryString();
        if (query != null) {
            logger.debug("Query string: {}", query);
            if (query.matches(".*(['\";]+|(--)+).*")) {
                logger.warn("Potential SQL Injection attempt detected in request: {}", requestUrl);
                throw new ServletException("Potential SQL Injection attempt detected!");
            }
        }
        String contentType = httpRequest.getContentType();
        if (contentType != null) {
            logger.debug("Content-Type: {}", contentType);
            if (contentType.contains("text/html")) {
                logger.warn("Potential XSS attempt detected in request payload for: {}", requestUrl);
                throw new ServletException("Potential XSS detected in request payload!");
            }
        }
        logger.info("Request passed WAF checks: {}", requestUrl);
        chain.doFilter(request, response);
    }
}

