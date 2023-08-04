package com.gatech.gameswap.model;

public class Swap {
	String proposerID;
	String counterPartyID;
	Long proposerItemID;
	Long counterPartyItemID;
	
	public String getProposerID() {
		return proposerID;
	}
	public void setProposerID(String proposerID) {
		this.proposerID = proposerID;
	}
	public String getCounterPartyID() {
		return counterPartyID;
	}
	public void setCounterPartyID(String counterPartyID) {
		this.counterPartyID = counterPartyID;
	}
	public Long getProposerItemID() {
		return proposerItemID;
	}
	public void setProposerItemID(Long proposerItemID) {
		this.proposerItemID = proposerItemID;
	}
	public Long getCounterPartyItemID() {
		return counterPartyItemID;
	}
	public void setCounterPartyItemID(Long counterPartyItemID) {
		this.counterPartyItemID = counterPartyItemID;
	}
	
	
}
