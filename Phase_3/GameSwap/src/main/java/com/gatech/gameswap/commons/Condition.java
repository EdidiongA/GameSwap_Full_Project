package com.gatech.gameswap.commons;

public enum Condition {
	
	Mint("Mint"), 
	LikeNew("Like New"),
	LightlyUsed("Lightly Used"), 
	ModeratelyUsed("Moderately Used"), 
	HeavilyUsed("Heavily Used"), 
	Damaged("Damaged/Missing");

    private String condition;

    Condition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return this.condition;
    }    
}
