package com.roszak89.demoTesting.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("/home").permitAll();
                    auth.anyRequest().permitAll();
                })
                .formLogin(Customizer.withDefaults())
                .logout().logoutSuccessUrl("/home")
                .and()
                .csrf().disable()
                .build();
    }
}
