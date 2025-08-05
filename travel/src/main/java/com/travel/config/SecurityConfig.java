package com.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.travel.service.MyUserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final MyUserService userService;
	
    public SecurityConfig(MyUserService userService) {
        this.userService = userService;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);  // 여기 주입!
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
            .getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(authenticationProvider())
            .build();
    }
	
	//스프링 시큐리티는 기본적으로 모든 요청을 인증 요구로 막기 때문에
	//최소한 SecurityFilterChain 빈을 만들어주거나 기본 설정을 해줘야 합니다.
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())  // POST 요청에 CSRF 토큰 없이 허용
            .authorizeHttpRequests(auth -> auth.requestMatchers("/users/form", "/users/login").permitAll().anyRequest().authenticated());
            //.formLogin();
        return http.build();
    }
	
	

    
	
	
	
	
	 
}
