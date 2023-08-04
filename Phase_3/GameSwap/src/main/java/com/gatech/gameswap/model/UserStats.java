package com.gatech.gameswap.model;

public class UserStats {
	private String rating;
	private Integer unacceptedSwapCount;
	private Integer unratedSwapCount;
	private Boolean unAccpetedDays;
	@Override
	public String toString() {
		return "UserStats [rating=" + rating + ", unacceptedSwapCount=" + unacceptedSwapCount + ", unratedSwapCount="
				+ unratedSwapCount + "]";
	}
	
	public Boolean getUnAccpetedDays() {
		return unAccpetedDays;
	}

	public void setUnAccpetedDays(Boolean unAccpetedDays) {
		this.unAccpetedDays = unAccpetedDays;
	}

	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public Integer getUnacceptedSwapCount() {
		return unacceptedSwapCount;
	}
	public void setUnacceptedSwapCount(Integer unacceptedSwapCount) {
		this.unacceptedSwapCount = unacceptedSwapCount;
	}
	public Integer getUnratedSwapCount() {
		return unratedSwapCount;
	}
	public void setUnratedSwapCount(Integer unratedSwapCount) {
		this.unratedSwapCount = unratedSwapCount;
	}
}
