package com.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.domain.User;
import com.travel.mapper.UserMapper;

@Service
public class UserService {
	private final UserMapper usermapper;
	
	public UserService(UserMapper usermapper) {
		this.usermapper = usermapper;
	}
	
	public List<User> getUserByNo(){
		return usermapper.getUserByNo();
		
	}
	
	public List<User> getUserByUserNo(int userNo){
		return usermapper.getUserByUserNo(userNo);
	}
	
	public int createUser(User user) {
		return usermapper.insertUser(user);
	}
	
	public int modifyUser(User user) {
		return usermapper.updateUser(user);
	}
	
	public int deleteUser(int userNo) {
		return usermapper.deleteUser(userNo);
	}
}
