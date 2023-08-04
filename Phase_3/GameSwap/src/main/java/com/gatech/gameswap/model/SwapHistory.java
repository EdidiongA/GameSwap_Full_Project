package com.gatech.gameswap.model;

import java.sql.Date;

public class SwapHistory {
	
	private Long swap_id;
	private String my_role;
	private Date proposed_Date;
	private Date acknowledged_date;
	private String swap_status;
	private String proposed_item;
	private String desired_item;
	private String other_user;
	private String other_user_id;
	private String rating;
	
	
	
	
	public String getOther_user_id() {
		return other_user_id;
	}
	public void setOther_user_id(String other_user_id) {
		this.other_user_id = other_user_id;
	}
	public Long getSwap_id() {
		return swap_id;
	}
	public void setSwap_id(Long swap_id) {
		this.swap_id = swap_id;
	}
	public String getMy_role() {
		return my_role;
	}
	public void setMy_role(String my_role) {
		this.my_role = my_role;
	}
	public Date getProposed_Date() {
		return proposed_Date;
	}
	public void setProposed_Date(Date proposed_Date) {
		this.proposed_Date = proposed_Date;
	}
	public Date getAcknowledged_date() {
		return acknowledged_date;
	}
	public void setAcknowledged_date(Date acknowledged_date) {
		this.acknowledged_date = acknowledged_date;
	}
	public String getSwap_status() {
		return swap_status;
	}
	public void setSwap_status(String swap_status) {
		this.swap_status = swap_status;
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
	public String getRating() {
		return rating;
	}
	public void setRating(String string) {
		this.rating = string;
	}
	
	
	
}
