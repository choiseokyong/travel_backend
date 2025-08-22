package com.travel.config;

import java.util.Arrays;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.travel.service.MyUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final MyUserService userService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;	// jwt 검증 필터 등록
	
    public SecurityConfig(MyUserService userService,JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
        	.cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/users/form", "/users/login", "/plans/share/**","/users/auth/refresh/**","/users/logout").permitAll()	//모두 허용
            		.requestMatchers("/admin/**").hasRole("ADMIN")			// admin 권한 필요
            		.requestMatchers("/users/**","/plans/**").hasAnyRole("USER","ADMIN")	// users 또는 admin 권한 필요
            		.anyRequest().authenticated()							// 나머지 요청은 인증된 사용자만 허용
            		).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            //.formLogin();
        return http.build();
    }
	
	 // ✅ CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // 프론트엔드 주소
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // 쿠키/인증정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    
	
	
	
	
	 
}
