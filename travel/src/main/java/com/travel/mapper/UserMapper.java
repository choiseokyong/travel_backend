package com.travel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.domain.User;

@Mapper
public interface UserMapper {
	// 회원등록
	public int insertUser(User user);
	
	// 회원 전체조회
	public List<User> getUserByNo();
	
	// 특정 회원조회
	public List<User> getUserByUserNo(int userNo);
	
	// 회원수정
	public int updateUser(User user);
	
	// 회원삭제
	public int deleteUser(int userNo);
}
