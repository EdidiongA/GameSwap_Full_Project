package com.gatech.gameswap.model;

public class SwapRating {
	private Long swap_id;
	private String accepted_date;
	private String myRole;
	private String proposed_item;
	private String desired_item;
	private String other_user;
	private String other_user_id;
	
	
	
	public Long getSwap_id() {
		return swap_id;
	}
	public void setSwap_id(Long swap_id) {
		this.swap_id = swap_id;
	}
	public String getOther_user_id() {
		return other_user_id;
	}
	public void setOther_user_id(String other_user_id) {
		this.other_user_id = other_user_id;
	}
	public String getAccepted_date() {
		return accepted_date;
	}
	public void setAccepted_date(String accepted_date) {
		this.accepted_date = accepted_date;
	}
	public String getMyRole() {
		return myRole;
	}
	public void setMyRole(String myRole) {
		this.myRole = myRole;
	}
	public String getProposed_item() {
		return proposed_item;
	}
	public void setProposed_item(String proposed_item) {
		this.proposed_item = proposed_item;
	}
	public String getDesired_item() {
		return desired_item;
	}
	public void setDesired_item(String desired_item) {
		this.desired_item = desired_item;
	}
	public String getOther_user() {
		return other_user;
	}
	public void setOther_user(String other_user) {
		this.other_user = other_user;
	}

	
	
}
