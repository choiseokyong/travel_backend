package com.travel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.domain.Plan;
import com.travel.mapper.PlanMapper;

@Service
public class PlanService {
	private final PlanMapper planmapper;
	
	public PlanService(PlanMapper planmapper){
		this.planmapper = planmapper;
	}
	
	public List<Plan> getPlanByUser(int userNo){
		return planmapper.getPlanByUserNo(userNo);
		
	}
	
	public int createPlan(Plan plan) {
		return planmapper.insertPlan(plan);
	}
	
	public int modifyPlan(int planNo) {
		return planmapper.updatePlan(planNo);
	}
	
	public int deletePlan(int planNo) {
		return planmapper.deletePlan(planNo);
	}
}
