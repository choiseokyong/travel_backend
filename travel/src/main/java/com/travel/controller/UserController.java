package com.travel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.domain.LoginRequest;
import com.travel.domain.LoginResponse;
import com.travel.domain.User;
import com.travel.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userservice;
	private final AuthenticationManager authenticationManager;
//	private final JwtTokenProvider jwtTokenProvider;
	
	public UserController(UserService userservice,AuthenticationManager authenticationManager) {
		this.userservice = userservice;
		this.authenticationManager = authenticationManager;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

//        String token = jwtTokenProvider.generateToken(authentication);
//        return ResponseEntity.ok(new LoginResponse(token));
		return null;
	}
	
	// 회원 전체 조회
	public List<User> getUserByNo() {
		return userservice.getUserByNo();
	}
	
	// 개인회원 조회
	@GetMapping("/{userNo}")
	public List<User> getUserByUserNo(@PathVariable int userNo) {
		return userservice.getUserByUserNo(userNo);
	}
	
	@PostMapping("/form")
    public int createUser(@RequestBody User user) {
        return userservice.createUser(user);
    }
	
	@GetMapping("/modify")
	public int modifyNo(@RequestBody User user) {
		return userservice.modifyUser(user);
	}
	
	@GetMapping("/delete/{userNo}")
	public int deleteNo(@PathVariable int userNo) {
		return userservice.deleteUser(userNo);
	}
	

}
