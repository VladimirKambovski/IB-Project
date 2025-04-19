package com.kambovski.ibproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Spring Security Configuration Beans.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    auth
                            // Само ADMIN има пристап до account management рутите
                            .requestMatchers("/account/**").hasRole("ADMIN")
                            // Само CUSTOMER има пристап до transaction рутите
                            .requestMatchers("/transaction/**").hasRole("CUSTOMER")
                            // Овозможуваме пристап до овие рути без автентикација
                            .requestMatchers("/", "/login", "/register", "/user/create","/account/details", "/account/withdraw", "/account/deposit").permitAll()
                            // Сите други бараат автентикација
                            .anyRequest().authenticated();
                })
                // X.509 автентикација со сертификати
                .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")  // Извлекување на корисничкото име (EMBG) од сертификатот
                .userDetailsService(userDetailsService)  // Користење на UserDetailsService за вчитување на корисникот од базата
                .and()
                .logout()
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");  // Пренасочување кон најава по одјавување -- ова не работи бидејќи го тргнав логин

        return http.build();
    }
}
