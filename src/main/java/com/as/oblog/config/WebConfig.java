package com.as.oblog.config;

import com.as.oblog.interceptors.LoginInterceptor;
import com.as.oblog.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * @author 12aaa-zone
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //设置不拦截的节点
        registry.addInterceptor(loginInterceptor).excludePathPatterns(
                "/user/login",
                "/user/register"
                );
    }
}
