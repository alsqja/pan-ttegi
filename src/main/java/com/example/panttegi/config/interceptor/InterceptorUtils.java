package com.example.panttegi.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;

public class InterceptorUtils {
    
    public static String extractPathVariable(HttpServletRequest request) {
        String path = request.getRequestURI();
        String[] pathSegments = path.split("/");

        return pathSegments[3];
    }
}

