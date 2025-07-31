package com.travel.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.domain.User;
import com.travel.mapper.UserMapper;

@Service
public class UserService {
	private final UserMapper usermapper;
	private final PasswordEncoder passwordencoder;	// 주입
	
	public UserService(UserMapper usermapper,PasswordEncoder passwordencoder) {
		this.usermapper = usermapper;
		this.passwordencoder = passwordencoder;
	}
	
	public List<User> getUserByNo(){
		return usermapper.getUserByNo();
		
	}
	
	public List<User> getUserByUserNo(int userNo){
		return usermapper.getUserByUserNo(userNo);
	}
	
	public int createUser(User user) {
		user.setPassWord(passwordencoder.encode(user.getPassWord()));
		return usermapper.insertUser(user);
	}
	
	public int modifyUser(User user) {
		return usermapper.updateUser(user);
	}
	
	public int deleteUser(int userNo) {
		return usermapper.deleteUser(userNo);
	}
}
