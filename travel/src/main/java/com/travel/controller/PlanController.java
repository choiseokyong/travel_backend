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
	
	// user 번호 가져오는 방법 좀 바꿔야 할듯
	@GetMapping("/{userNo}")
	public List<Plan> getPlanByUser(@PathVariable int userNo) {
		return planservice.getPlanByUser(userNo);
	}
	
	@PostMapping("/form")
    public int createPlan(@RequestBody PlanResponseDTO planresponsedto) {
        return planservice.createPlan(planresponsedto);
    }
	
	@GetMapping("/modify/{no}")
	public int modifyNo(@RequestBody PlanResponseDTO planresponsedto) {
		return planservice.modifyPlan(planresponsedto);
	}
	
	// plan 삭제
	@GetMapping("/delete/{no}")
	public int deleteNo(@PathVariable int planNo) {
		return planservice.deletePlan(planNo);
	}
	
	@GetMapping("/delete/item/{no}")
	public int deleteItemNo(@PathVariable int planItemNo) {
		return planservice.deletePlanItem(planItemNo);
	}
	
}
