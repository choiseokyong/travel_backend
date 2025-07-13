package com.travel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.domain.Plan;
import com.travel.domain.User;
import com.travel.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userservice;
	
	public UserController(UserService userservice) {
		this.userservice = userservice;
	}
	
	public List<User> getUserByNo() {
		return userservice.getUserByNo();
	}
	
	@GetMapping("/{userNo}")
	public List<User> getUserByUserNo(@PathVariable int userNo) {
		return userservice.getUserByUserNo(userNo);
	}
	
	@PostMapping("/form")
    public int createUser(@RequestBody User user) {
        return userservice.createUser(user);
    }
	
	@GetMapping("/modify/{no}")
	public int modifyNo(@PathVariable int userNo) {
		return userservice.modifyUser(userNo);
	}
	
	@GetMapping("/delete/{no}")
	public int deleteNo(@PathVariable int userNo) {
		return userservice.deleteUser(userNo);
	}
	

}
