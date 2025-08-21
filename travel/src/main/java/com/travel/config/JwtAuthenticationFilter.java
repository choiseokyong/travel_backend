package com.travel.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.travel.security.JwtTokenProvider;
import com.travel.security.JwtTokenProvider.TokenStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // 토큰 유틸 클래스

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	String uri = request.getRequestURI();

    	if (uri.equals("/favicon.ico") || 
    	    (uri.startsWith("/plans/share/") && !uri.startsWith("/plans/share/new/"))) {
    	    filterChain.doFilter(request, response); // JWT 검사 건너뛰기
    	    return;
    	}

        String token = jwtTokenProvider.resolveToken(request); // 헤더에서 토큰 꺼내기
        
        	if (token != null) {
        		TokenStatus status = jwtTokenProvider.validateToken(token);
        		
        		switch(status) {
	        		case VALID:
	        			Authentication auth = jwtTokenProvider.getAuthentication(token);
	                    SecurityContextHolder.getContext().setAuthentication(auth);
	                    break;
	                    
	        		case EXPIRED:
	        			// Access Token 만료 → Refresh Token 갱신 필요
	                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
	                    response.getWriter().write("Access token expired");
	                    return; // 더 진행하지 않음
	        		case INVALID:
	        			// 유효하지 않은 토큰
	                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
	                    response.getWriter().write("Invalid token");
	                    return;
        		}
                
            }
        
        filterChain.doFilter(request, response);
    }
}
