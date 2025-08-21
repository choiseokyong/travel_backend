package com.travel.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.domain.LoginRequest;
import com.travel.domain.LoginResponse;
import com.travel.domain.MyUser;
import com.travel.security.JwtTokenProvider;
import com.travel.security.JwtTokenProvider.TokenStatus;
import com.travel.service.MyUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/users")
public class MyUserController {
	private final MyUserService userservice;
	private final AuthenticationManager authenticationManager; // 여기에서 주입받기
	private final JwtTokenProvider jwtTokenProvider;
	
	public MyUserController(MyUserService userservice,AuthenticationManager authenticationManager,JwtTokenProvider jwtTokenProvider) {
		this.userservice = userservice;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	
	@PostMapping("/auth/refresh")
	public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String testrefreshToken,HttpServletRequest request) {
		
		// 쿠키에서 Refresh Token 추출
	    String refreshToken = Arrays.stream(request.getCookies())
	            .filter(c -> "refreshToken".equals(c.getName()))
	            .findFirst()
	            .map(Cookie::getValue)
	            .orElse(null);
	    
	    // Refresh 토큰 검사
	    TokenStatus refreshStatus = jwtTokenProvider.validateRefreshToken(refreshToken);
	    System.out.println("refreshStatus: " + refreshStatus);
	    switch(refreshStatus) {
	    case VALID:break;
	    case EXPIRED:
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired, please login again");
	    case INVALID:
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
	    }
	    
	    
//	    generateToken(String username, List<String> roles) → 로그인 시 발급 토큰과 동일 구조로 생성
//
//	    Refresh Token에서 username + roles를 추출 → 새 Access Token 생성
//
//	    getRolesFromClaims로 타입 안전하게 roles 추출
//
//	    SecurityContext에서 권한 문제 없이 사용 가능 ✅

	    // Refresh Token 검증 성공 → 새 Access Token 발급
	    String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
	    Claims claims = jwtTokenProvider.getClaimsFromToken(refreshToken);
	    List<String> roles = jwtTokenProvider.getRolesFromClaims(claims);

	    String newAccessToken = jwtTokenProvider.generateToken(username,roles);

	    return ResponseEntity.ok(new LoginResponse(newAccessToken));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			
			Authentication authentication = authenticationManager.authenticate( 	//사용자 검증 전체 진행
	            new UsernamePasswordAuthenticationToken(	// 로그인 요청 객체
	                request.getEmail(),
	                request.getPassWord()
	            )
	            
	        );
		    String token = jwtTokenProvider.generateToken(authentication);
		 // Refresh Token 생성 (긴 만료)
	        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
	        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
	                .httpOnly(true)
	                .secure(false)   // HTTPS 환경이면 true
	                .path("/")
	                .maxAge(7 * 24 * 60 * 60) // 7일
	                .build();
	        
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new LoginResponse(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
	}
	
	// 회원 전체 조회
	public List<MyUser> getUserByNo() {
		return userservice.getUserByNo();
	}
	
	// 개인회원 조회
	@GetMapping("/{userNo}")
	public List<MyUser> getUserByUserNo(@PathVariable int userNo) {
		return userservice.getUserByUserNo(userNo);
	}
	
	@PostMapping("/form")
    public int createUser(@RequestBody MyUser user) {
		if(user.getGrade() == null) {
			user.setGrade(1);
		}
        return userservice.createUser(user);
    }
	
	@GetMapping("/modify")
	public int modifyNo(@RequestBody MyUser user) {
		
		return userservice.modifyUser(user);
	}
	
	@GetMapping("/delete/{userNo}")
	public int deleteNo(@PathVariable int userNo) {
		return userservice.deleteUser(userNo);
	}
	

}
