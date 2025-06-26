package com.iris.OnlineCompilerBackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private AccessTokenFilter accessTokenFilter;

    @Bean
    public FilterRegistrationBean<AccessTokenFilter> accessTokenFilterBean() {
        FilterRegistrationBean<AccessTokenFilter> accessTokenFilterFilterRegistrationBean = new FilterRegistrationBean<>();

        accessTokenFilterFilterRegistrationBean.setFilter(accessTokenFilter);

        accessTokenFilterFilterRegistrationBean.addUrlPatterns("/*");

        accessTokenFilterFilterRegistrationBean.setOrder(1);

        return accessTokenFilterFilterRegistrationBean;
    }
}