package com.travel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
import com.travel.service.MyUserService;


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
            return ResponseEntity.ok(new LoginResponse(token));
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
