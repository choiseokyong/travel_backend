package com.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	//스프링 시큐리티는 기본적으로 모든 요청을 인증 요구로 막기 때문에
	//최소한 SecurityFilterChain 빈을 만들어주거나 기본 설정을 해줘야 합니다.
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())  // POST 요청에 CSRF 토큰 없이 허용
            .authorizeHttpRequests(auth -> auth.requestMatchers("/users/form").permitAll().anyRequest().authenticated())
            .httpBasic();
            //.formLogin();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	 
}
