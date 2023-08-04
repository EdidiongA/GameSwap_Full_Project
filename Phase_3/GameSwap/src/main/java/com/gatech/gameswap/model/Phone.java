package com.gatech.gameswap.model;


public class Phone {

	public enum PhoneNumberType {
		Home, Work, Mobile
	}
	private String number;
	private PhoneNumberType type;
	private boolean share;
	
	@Override
	public String toString() {
		return "Phone [number=" + number + ", type=" + type + ", share=" + share + "]";
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public PhoneNumberType getType() {
		return type;
	}
	public void setType(PhoneNumberType type) {
		this.type = type;
	}
	public boolean isShare() {
		return share;
	}
	public void setShare(boolean share) {
		this.share = share;
	}
}
