package com.travel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.travel.domain.LoginRequest;
import com.travel.domain.MyUser;
import com.travel.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userservice;
	
	
	public UserController(UserService userservice) {
		this.userservice = userservice;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
            return ResponseEntity.ok(userservice.login(request));
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
