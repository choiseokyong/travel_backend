package com.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.domain.Plan;
import com.travel.domain.PlanItem;
import com.travel.domain.PlanResponseDTO;
import com.travel.mapper.PlanMapper;

//@Transactional 오류 시 자동 롤백
@Service
public class PlanService {
	private final PlanMapper planmapper;
	private int flag;
	
	public PlanService(PlanMapper planmapper){
		this.planmapper = planmapper;
	}
	
	public List<Plan> getPlanByUser(int userNo){
		return planmapper.getPlanByUserNo(userNo);
		
	}
	
	public int createPlan(PlanResponseDTO planresponsedto) {
		Plan plan = toPlan(planresponsedto);
		flag = planmapper.insertPlan(plan);
		
		for(PlanItem item : planresponsedto.getItem()) {
			planmapper.insertPlanItem(toPlanItem(item,plan.getNo()));
		}
		
		return flag;
		
	}
	
	// planitem 수정, 삭제 관련... 어찌??
	public int modifyPlan(PlanResponseDTO planresponsedto) {
		Plan plan = toPlan(planresponsedto);
		flag = planmapper.updatePlan(plan.getNo());
		
		for(PlanItem item : planresponsedto.getItem()) {
			planmapper.updatePlanItem(toPlanItem(item,plan.getNo()));
		}
		
		return flag;
		
		
	}
	
	public int deletePlan(int planNo) {
		return planmapper.deletePlan(planNo);
	}
	
	public int deletePlanItem(int planItemNo) {
		return planmapper.deletePlanItem(planItemNo);
	}
	
	private Plan toPlan(PlanResponseDTO planresponsedto) {
		Plan plan = new Plan();
		plan.setTitle(planresponsedto.getTitle());
		plan.setStartDate(planresponsedto.getStartDate());
		plan.setEndDate(planresponsedto.getEndDate());
		plan.setUserNo(planresponsedto.getUserNo());
		
		return plan;
	}
	
	private PlanItem toPlanItem(PlanItem item, int planNo) {
		PlanItem planItem = new PlanItem();
		planItem.setLat(item.getLat());
		planItem.setLng(item.getLng());
		planItem.setMemo(item.getMemo());
		planItem.setPlace(item.getPlace());
		planItem.setPlanDate(item.getPlanDate());
		planItem.setPlanSort(item.getPlanSort());
		planItem.setPlanNo(planNo);
		
		return planItem;
	}
}
