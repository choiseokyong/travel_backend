package com.travel.domain;

import java.util.List;

public class PlanResponseDTO {
	private int no;
	private String title;
	private String startDate;
	private String endDate;
	private String regDate;
	private int userNo; // 유저번호
	private List<PlanItem> item;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public List<PlanItem> getItem() {
		return item;
	}
	public void setItem(List<PlanItem> item) {
		this.item = item;
	}
	
	
	
}
