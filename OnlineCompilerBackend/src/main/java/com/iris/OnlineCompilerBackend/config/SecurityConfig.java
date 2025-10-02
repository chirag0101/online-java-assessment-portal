//package com.iris.OnlineCompilerBackend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> {})
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/AdminService/authenticate-admin").permitAll()
//                        .requestMatchers("/CompilerService/**").permitAll()
//                        .requestMatchers("/AssessmentService/assessment").permitAll()
//                        .requestMatchers("/AssessmentService/end-assessment/**").permitAll()
//                        .requestMatchers("/AssessmentService/assessment-end-time-by-candidate-id/**").permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }
//}
