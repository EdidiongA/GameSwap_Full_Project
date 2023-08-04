package com.gatech.gameswap.model;

public class ItemOwner {

	private User user;
	private Double distance;
	private Double rating;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	@Override
	public String toString() {
		return "ItemOwner [user=" + user + ", distance=" + distance + ", rating=" + rating + "]";
	}


}
