package com.example.activity_service.common.config;

import com.example.activity_service.client.UserServiceClient;
import com.example.activity_service.common.jwt.JWTFilter;
import com.example.activity_service.common.jwt.JWTUtil;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    private final JWTUtil jwtUtil;

    private final UserServiceClient userServiceClient;

    public SecurityConfig(JWTUtil jwtUtil, JWTFilter jwtFilter,UserServiceClient userServiceClient) {

        this.jwtFilter = jwtFilter;
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable);

        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable);

        // 인증 인가 관련
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/activity/internal/**","/activity/errorful/**","/activity/test/**"
                        ).permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );


        httpSecurity
                .addFilterBefore(new JWTFilter(jwtUtil,userServiceClient),UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();

    }

    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterFilterRegistrationBean(){
        FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/activity/*"); // 필터를 어떤 URL에 적용할지 지정
        return registrationBean;
    }



}
