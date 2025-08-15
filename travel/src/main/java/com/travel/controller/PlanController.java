package com.travel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.domain.Plan;
import com.travel.domain.PlanResponseDTO;
import com.travel.service.PlanService;


@RestController
@RequestMapping("/plans")
public class PlanController {
	private final PlanService planservice;
	
	public PlanController(PlanService planservice) {
		this.planservice = planservice;
	}
	
	// plan 조회
	@GetMapping("/list")
	public List<Plan> getPlan() {
		return planservice.getPlan();
	}
		
	// plan,planitem 상세 조회
	@GetMapping("/list/{planNo}")
	public PlanResponseDTO getPlanByPlanItem(@PathVariable int planNo) {
		return planservice.getPlanByPlanItem(planNo);
	}
	
	// plan,item 등록
	@PostMapping("/form")
    public int createPlan(@RequestBody PlanResponseDTO planresponsedto) {
        return planservice.createPlan(planresponsedto);
    }
	
	// plan,item 수정
	@GetMapping("/modify")
	public int modifyNo(@RequestBody PlanResponseDTO planresponsedto) {
		return planservice.modifyPlan(planresponsedto);
	}
	
	// plan 삭제
	@GetMapping("/delete/{planNo}")
	public int deleteNo(@PathVariable int planNo) {
		return planservice.deletePlan(planNo);
	}
	
	// planitem 삭제
	@GetMapping("/delete/item/{planItemNo}")
	public int deleteItemNo(@PathVariable int planItemNo) {
		return planservice.deletePlanItem(planItemNo);
	}
	
}
