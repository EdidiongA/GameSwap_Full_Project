package com.gatech.gameswap.model;

import java.sql.Date;

public class SwapDetail {

	private String my_role;
	private Date proposed_Date;
	private Date acknowledged_date;
	private String swap_status;
	private String rating;
	
	private String distance;
	private String email;
	private String firstName;
	private String nickName;
	private Phone phone;
	
	private Item proposed_Item;
	private Item desired_Item;
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
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}
	public Item getProposed_Item() {
		return proposed_Item;
	}
	public void setProposed_Item(Item proposed_Item) {
		this.proposed_Item = proposed_Item;
	}
	public Item getDesired_Item() {
		return desired_Item;
	}
	public void setDesired_Item(Item desired_Item) {
		this.desired_Item = desired_Item;
	}
	
	
	
	
}
