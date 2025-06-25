package com.iris.OnlineCompilerBackend.config;

import com.iris.OnlineCompilerBackend.models.Admin;
import com.iris.OnlineCompilerBackend.repositories.AdminRepo;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Configuration
public class AccessTokenFilter implements Filter {

    private final AdminRepo adminRepo;

    Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    @Value("${zone.id}")
    String timeZone;

    public AccessTokenFilter(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String method = httpServletRequest.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "accessToken, adminId");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        List<String> paths = List.of(
                "/AdminService/authenticate-admin",
                "/AdminService/reset-password",
                "/AssessmentService/assessment"
        );

        String path = httpServletRequest.getRequestURI();

        if (paths.contains(path) || path.contains("/CompilerService") || path.contains("/AssessmentService/end-assessment")) {
            filterChain.doFilter(httpServletRequest, httpResponse);
            return;
        }

        logger.info(httpServletRequest.getHeader("accessToken"));

        String accessToken = httpServletRequest.getHeader("accessToken");

        String userId = httpServletRequest.getHeader("adminId");

        if ((accessToken == null || accessToken.isEmpty()) || ((userId == null) || (userId.isEmpty()))) {
            logger.warn("Missing accessToken header. Blocking request.");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Admin admin = adminRepo.findByAdminId(userId);

        OffsetDateTime offsetDateTime = Instant.now().atZone(ZoneId.of(timeZone)).toOffsetDateTime();
        String formattedWithOffset = offsetDateTime.toString();

        admin.setAccessTokenLastAccessedOn(formattedWithOffset);

        adminRepo.save(admin);

        if ((admin.getAdminId().equals(userId)) && (admin.getLastAccesstoken().equals(accessToken)) && (admin.getAccessTokenIsExpired() == false)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
