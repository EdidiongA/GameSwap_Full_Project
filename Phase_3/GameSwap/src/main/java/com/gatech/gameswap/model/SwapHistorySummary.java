package com.gatech.gameswap.model;

public class SwapHistorySummary {
	private String my_role;
	private int total_count;
	private int accepted_count;
	private int rejected_count;
	private String rejected_percentage;
	
	
	public String getMy_role() {
		return my_role;
	}
	public void setMy_role(String my_role) {
		this.my_role = my_role;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getAccepted_count() {
		return accepted_count;
	}
	public void setAccepted_count(int accepted_count) {
		this.accepted_count = accepted_count;
	}
	public int getRejected_count() {
		return rejected_count;
	}
	public void setRejected_count(int rejected_count) {
		this.rejected_count = rejected_count;
	}
	public String getRejected_percentage() {
		return rejected_percentage;
	}
	public void setRejected_percentage(String rejected_percentage) {
		this.rejected_percentage = rejected_percentage;
	}
	
	
}
