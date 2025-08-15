package com.travel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.domain.Plan;
import com.travel.domain.PlanItem;
import com.travel.domain.PlanResponseDTO;

@Mapper
public interface PlanMapper {
	// plan 등록
	public int insertPlan(Plan plan);
	
	// planItem 등록
	public int insertPlanItem(PlanItem planitem);
	
	// plan 조회
	public List<Plan> getPlan(int userNo);
	
	// plan 조회
	public Plan getPlanByOne(int planNo);
	
	
	// planItem 조회
	public List<PlanItem> getPlanItem(int planNo);
	
	// plan, planItem 조회
	public List<PlanResponseDTO> getPlanByPlanItem(int planNo);
	
	// plan 수정
	public int updatePlan(Plan plan);
	
	// planItem 수정
	public int updatePlanItem(PlanItem planitem);
	
	// plan 삭제
	public int deletePlan(int planNo);
	
	// planItem 삭제
	public int deletePlanItem(int planItemNo);
}
