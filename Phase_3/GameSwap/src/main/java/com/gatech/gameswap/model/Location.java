package com.gatech.gameswap.model;

public class Location {

	private String postalCode;
	private String city;
	private String state;
	
	@Override
	public String toString() {
		return "Location [postalCode=" + postalCode + ", city=" + city + ", state=" + state + "]";
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
