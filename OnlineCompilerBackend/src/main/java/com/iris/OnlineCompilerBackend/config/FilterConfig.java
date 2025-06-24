package com.iris.OnlineCompilerBackend.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    // Spring will now inject AccessTokenFilter as a bean
    private final AccessTokenFilter accessTokenFilter;

    // Use constructor injection for the AccessTokenFilter
    public FilterConfig(AccessTokenFilter accessTokenFilter) {
        this.accessTokenFilter = accessTokenFilter;
    }

    @Bean
    public FilterRegistrationBean<AccessTokenFilter> accessTokenFilterBean() {
        FilterRegistrationBean<AccessTokenFilter> accessTokenFilterFilterRegistrationBean = new FilterRegistrationBean<>();

        accessTokenFilterFilterRegistrationBean.setFilter(accessTokenFilter);

        accessTokenFilterFilterRegistrationBean.addUrlPatterns("/*");

        accessTokenFilterFilterRegistrationBean.setOrder(1);

        return accessTokenFilterFilterRegistrationBean;
    }
}