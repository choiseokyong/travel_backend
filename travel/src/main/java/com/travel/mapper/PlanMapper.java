package com.travel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.travel.domain.Plan;
import com.travel.domain.PlanItem;
import com.travel.domain.PlanRequestDTO;
import com.travel.domain.PlanResponseDTO;
import com.travel.domain.PlanShare;

@Mapper
public interface PlanMapper {
	
	// planshare 조회
	public PlanShare findByUuid(String shareUuid);
	// planshare 등록
	public int insertPlanShare(PlanShare planShare);
	
	// plan 등록
	public int insertPlan(Plan plan);
	
	// planItem 등록
	public int insertPlanItem(PlanItem planitem);
	
	// plan 조회
	public List<Plan> getPlan(@Param("userNo") int userNo,@Param("info") PlanRequestDTO planrequestdto);
	
	// plan 조회
	public int getPlanAllCount(int userNo);
	
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
