package com.travel.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.travel.domain.Plan;
import com.travel.domain.PlanItem;
import com.travel.domain.PlanRequestDTO;
import com.travel.domain.PlanResponseDTO;
import com.travel.domain.PlanShare;
import com.travel.mapper.MyUserMapper;
import com.travel.mapper.PlanMapper;

//@Transactional 오류 시 자동 롤백
@Service
public class PlanService {
	private final PlanMapper planmapper;
	private final MyUserMapper myusermapper;
	
	private int flag;
	
	public PlanService(PlanMapper planmapper,MyUserMapper myusermapper){
		this.planmapper = planmapper;
		this.myusermapper = myusermapper;
		
	}
	
	public Map<String, Object> getPlan(PlanRequestDTO planrequestdto){
		int userNo = getUserNo();
		int totalCount = planmapper.getPlanAllCount(userNo);
		
		planrequestdto.setTotalPage((totalCount + planrequestdto.getSize() -1) / planrequestdto.getSize());
		planrequestdto.setNowPage(planrequestdto.getPage());
		planrequestdto.setPage(planrequestdto.getPage()*planrequestdto.getSize());
		
		Map<String, Object> result = new HashMap<>();
		result.put("planList", planmapper.getPlan(userNo,planrequestdto));
		result.put("pageInfo", planrequestdto);
		return result;
	}
	/* share 관련*/
	
	// share 조회
	public PlanResponseDTO getSharedPlan(String uuid) {
		PlanShare share = planmapper.findByUuid(uuid);
		
		if (share == null || share.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("공유 링크가 만료되었거나 존재하지 않습니다.");
        }
		
		return getPlanByPlanItem(share.getPlanNo()); // 기존 일정 조회
	}
	// share 생성
	public String createPlanShare(Integer planNo) {
		String uuid = UUID.randomUUID().toString();
		
		PlanShare share = new PlanShare();
		share.setPlanNo(planNo);
		share.setShareUuid(uuid);
		share.setExpireDate(LocalDateTime.now().plusDays(7)); // 7일 유효
		planmapper.insertPlanShare(share);
		return "http://localhost:5173/plans/share/" + uuid;
	}
	
	/* plan 관련 */
	public PlanResponseDTO getPlanByPlanItem(int planNo){
		PlanResponseDTO prDTO = new PlanResponseDTO();
		Plan plan = planmapper.getPlanByOne(planNo);
		
		prDTO.setNo(plan.getNo());
		prDTO.setTitle(plan.getTitle());
		prDTO.setStartDate(plan.getStartDate());
		prDTO.setEndDate(plan.getEndDate());
		prDTO.setUserNo(plan.getUserNo());
		prDTO.setMemo(plan.getMemo());
		
		List<PlanItem> planItem = planmapper.getPlanItem(planNo);
		prDTO.setItem(planItem);
		
		return prDTO;
		
	}
	
	public int createPlan(PlanResponseDTO planresponsedto) {
		// 현재 로그인한 사용자 정보 가져오기 (컨트롤러에선 파라미터로 받아서 이메일 구할 수 있음)
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = (String) authentication.getPrincipal();  // JwtTokenProvider에서 Username으로 세팅한 값
	    int userNo = myusermapper.findByEmail(email).getNo();      // 이메일로 user_no 조회
	    
	    // Plan 객체 생성 시 userNo 설정
		Plan plan = toPlan(planresponsedto);
		plan.setUserNo(userNo);
		flag = planmapper.insertPlan(plan);
		
		// PlanItem 등록
		for(PlanItem item : planresponsedto.getItem()) {
			planmapper.insertPlanItem(toPlanItem(item,plan.getNo()));
		}
		
		return flag;
		
	}
	
	// plan
	public int modifyPlan(PlanResponseDTO planresponsedto) {
		Plan plan = toPlan(planresponsedto);
		flag = planmapper.updatePlan(plan);
		for(PlanItem item : planresponsedto.getItem()) {
			// planitem 있는지 없는지 확인해서 수정 또는 등록 되게 작업 필요.
			
			if(item.getNo() != null) {
				planmapper.updatePlanItem(toPlanItem(item,plan.getNo()));
			}else {
				
				planmapper.insertPlanItem(toPlanItem(item,plan.getNo()));
			}
			
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
		plan.setNo(planresponsedto.getNo());
		plan.setTitle(planresponsedto.getTitle());
		plan.setStartDate(planresponsedto.getStartDate());
		plan.setEndDate(planresponsedto.getEndDate());
		plan.setUserNo(planresponsedto.getUserNo());
		plan.setMemo(planresponsedto.getMemo());
		
		return plan;
	}
	
	private PlanItem toPlanItem(PlanItem item, int planNo) {
		PlanItem planItem = new PlanItem();
		planItem.setNo(item.getNo());
		planItem.setDay(item.getDay());
		planItem.setLat(item.getLat());
		planItem.setLng(item.getLng());
		planItem.setMemo(item.getMemo());
		planItem.setPlace(item.getPlace());
		planItem.setPlanDate(item.getPlanDate());
		planItem.setPlanSort(item.getPlanSort());
		planItem.setPlanNo(planNo);
		
		return planItem;
	}
	
	private int getUserNo() {
		// 현재 로그인한 사용자 정보 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = (String) authentication.getPrincipal();  // JwtTokenProvider에서 Username으로 세팅한 값
	    return myusermapper.findByEmail(email).getNo();      // 이메일로 user_no 조회
	}
}
