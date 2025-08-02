package com.travel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.domain.LoginRequest;
import com.travel.domain.MyUser;

@Mapper
public interface UserMapper {
	// 로그인
	public LoginRequest findByEmail(String email);
	
	// 회원등록
	public int insertUser(MyUser user);
	
	// 회원 전체조회
	public List<MyUser> getUserByNo();
	
	// 특정 회원조회
	public List<MyUser> getUserByUserNo(int userNo);
	
	// 회원수정
	public int updateUser(MyUser user);
	
	// 회원삭제
	public int deleteUser(int userNo);
}
