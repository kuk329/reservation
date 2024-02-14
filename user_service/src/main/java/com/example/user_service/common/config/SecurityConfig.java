package com.example.user_service.common.config;

import com.example.user_service.common.filter.JWTFilter;
import com.example.user_service.common.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTFilter jwtFilter;


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
                        .requestMatchers("/users/signup", "/users/email-certification",
                                "/users/login", "/users/internal/**","/users/test/**","/main"
                        ).permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );

        httpSecurity
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterRegistrationBean(){
        FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/*"); // 필터를 어떤 URL에 적용할지 지정
        return registrationBean;
    }



}
