package com.iris.OnlineCompilerBackend.config;

import com.iris.OnlineCompilerBackend.models.Admin;
import com.iris.OnlineCompilerBackend.repositories.AdminRepo;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Configuration
public class AccessTokenFilter implements Filter {

    @Autowired
    private AdminRepo adminRepo;

    @Value("${zone.id}")
    String timeZone;

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
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, accessToken, adminId");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        List<String> paths = List.of(
                "/AdminService/authenticate-admin",
                "/AdminService/reset-password",
                "/AssessmentService/assessment"
        );

        String path = httpServletRequest.getRequestURI();

        if (paths.contains(path) || path.contains("/CompilerService") || path.contains("/AssessmentService/end-assessment/") || path.contains("AssessmentService/assessment-end-time-by-candidate-id")) {
            filterChain.doFilter(httpServletRequest, httpResponse);
            return;
        }

        String accessToken = httpServletRequest.getHeader("accessToken");

        String userId = httpServletRequest.getHeader("adminId");

        if ((accessToken == null || accessToken.isEmpty()) || ((userId == null) || (userId.isEmpty()))) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Admin admin = adminRepo.findByAdminId(userId);

        if (admin == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if ((admin.getAdminId().equals(userId)) && (admin.getLastAccesstoken().equals(accessToken)) && (admin.getAccessTokenIsExpired() == false) ) {
            OffsetDateTime offsetDateTime = Instant.now().atZone(ZoneId.of(timeZone)).toOffsetDateTime();
            String formattedWithOffset = offsetDateTime.toString();

            admin.setAccessTokenLastAccessedOn(formattedWithOffset);

            adminRepo.save(admin);
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
