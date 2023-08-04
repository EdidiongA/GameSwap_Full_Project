package com.gatech.gameswap.model;

public class SwapAck {
	private String proposed_date;
	private String proposer;
	private String proposed_item;
	private String desired_item;
	private int proposed_item_id;
	private int counterparty_item_id;
	private String distance;
	private String Rating;
	
	
	public int getProposed_item_id() {
		return proposed_item_id;
	}
	public void setProposed_item_id(int proposed_item_id) {
		this.proposed_item_id = proposed_item_id;
	}
	public int getCounterparty_item_id() {
		return counterparty_item_id;
	}
	public void setCounterparty_item_id(int counterparty_item_id) {
		this.counterparty_item_id = counterparty_item_id;
	}
	public String getProposed_date() {
		return proposed_date;
	}
	public void setProposed_date(String proposed_date) {
		this.proposed_date = proposed_date;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
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
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getRating() {
		return Rating;
	}
	public void setRating(String rating) {
		Rating = rating;
	}
	
	
}
