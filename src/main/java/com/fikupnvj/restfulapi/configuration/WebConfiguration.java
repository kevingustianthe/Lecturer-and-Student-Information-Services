package com.fikupnvj.restfulapi.configuration;

import com.fikupnvj.restfulapi.resolver.AdminArgumentResolver;
import com.fikupnvj.restfulapi.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Autowired
    private AdminArgumentResolver adminArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(adminArgumentResolver);
        resolvers.add(userArgumentResolver);
    }
}
