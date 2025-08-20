package com.travel.domain;

import java.time.LocalDateTime;

public class PlanShare {
	private Integer no;
	private Integer planNo;
	private String shareUuid;
	private LocalDateTime expireDate;
	private String regDate;
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Integer getPlanNo() {
		return planNo;
	}
	public void setPlanNo(Integer planNo) {
		this.planNo = planNo;
	}
	public String getShareUuid() {
		return shareUuid;
	}
	public void setShareUuid(String shareUuid) {
		this.shareUuid = shareUuid;
	}
	public LocalDateTime getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(LocalDateTime expireDate) {
		this.expireDate = expireDate;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
}
