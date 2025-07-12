package com.travel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.domain.Plan;

@Mapper
public interface PlanMapper {
	// 회원가입
	
	// 회원정보 조회
	
	// plan 등록
	public int insertPlan(Plan plan);
	
	// plan 조회
	public List<Plan> getPlanByUserNo(int userNo);
	
	// plan 수정
	public int updatePlan(int planNo);
	
	// plan 삭제
	public int deletePlan(int planNo);
}
