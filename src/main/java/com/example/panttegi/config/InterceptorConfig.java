package com.example.panttegi.config;

import com.example.panttegi.config.interceptor.BoardRoleInterceptor;
import com.example.panttegi.config.interceptor.WorkspaceRoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final WorkspaceRoleInterceptor workspaceRoleInterceptor;
    private final BoardRoleInterceptor boardRoleInterceptor;

    private static final String[] WORKSPACE_PATH_LIST = {"/api/workspaces"};
    private static final String[] BOARD_PATH_LIST = {"/api/workspaces/**"};
    private static final String[] BOARD_WHITE_LIST = {"/api/workspaces"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(workspaceRoleInterceptor)
//                .addPathPatterns(WORKSPACE_PATH_LIST)
//                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(boardRoleInterceptor)
                .addPathPatterns(BOARD_PATH_LIST)
                .excludePathPatterns(BOARD_WHITE_LIST)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);
    }
}
