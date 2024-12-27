package com.example.panttegi.config.interceptor;

import com.example.panttegi.util.AuthenticationScheme;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> variables = new HashMap<>();

        for (int i = 0; i < pathSegments.length; i++) {
            if (pathSegments[i].startsWith("{") && pathSegments[i].endsWith("}")) {
                String key = pathSegments[i].substring(1, pathSegments[i].length() - 1);
                variables.put(key, pathSegments[i + 1]);
            }
        }
        return variables.get(variableName);
    }
}

