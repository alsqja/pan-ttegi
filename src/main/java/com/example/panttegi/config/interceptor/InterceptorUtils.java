package com.example.panttegi.config.interceptor;

import com.example.panttegi.util.AuthenticationScheme;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class InterceptorUtils {

    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

        boolean tokenFound = StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix);

        if (tokenFound) {
            return bearerToken.substring(headerPrefix.length());
        }

        return null;
    }

    public static String extractPathVariable(HttpServletRequest request, String variableName) {
        String path = request.getRequestURI();
        String[] pathSegments = path.split("/");

        return pathSegments[3];
    }
}

