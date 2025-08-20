package com.travel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.domain.Plan;
import com.travel.domain.PlanResponseDTO;
import com.travel.domain.PlanShare;
import com.travel.service.EmailService;
import com.travel.service.PlanService;

import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/plans")
public class PlanController {
	private final PlanService planservice;
	private final EmailService mailSender;
	
	public PlanController(PlanService planservice,EmailService mailSender) {
		this.planservice = planservice;
		this.mailSender = mailSender;
	}
	
	// 공유 링크 접근 api
	@GetMapping("/share/{uuid}")
	public PlanResponseDTO getSharedPlan(@PathVariable String uuid){
		return planservice.getSharedPlan(uuid);
	}
	
	// 공유 링크 생성 api
	@PostMapping("/share/new/{planNo}")
	public ResponseEntity<String> createShare(@PathVariable Integer planNo){
		
		String link = planservice.createPlanShare(planNo);
		try {
			// 현재 로그인한 사용자 정보 가져오기
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String email = (String) authentication.getPrincipal();  // JwtTokenProvider에서 Username으로 세팅한 값
		    
			mailSender.sendShareLink("ggt9875654@naver.com", link,"naver");
//			mailSender.sendShareLink(email, link);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(link);
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
	@PutMapping("/modify")
	public int modifyNo(@RequestBody PlanResponseDTO planresponsedto) {
		return planservice.modifyPlan(planresponsedto);
	}
	
	// plan 삭제
	@DeleteMapping("/delete/{planNo}")
	public int deleteNo(@PathVariable int planNo) {
		return planservice.deletePlan(planNo);
	}
	
	// planitem 삭제
	@DeleteMapping("/delete/item/{planItemNo}")
	public int deleteItemNo(@PathVariable int planItemNo) {
		return planservice.deletePlanItem(planItemNo);
	}
	
}
