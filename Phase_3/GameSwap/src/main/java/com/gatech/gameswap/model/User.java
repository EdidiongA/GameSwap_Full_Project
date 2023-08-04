package com.gatech.gameswap.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class User {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Location location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String firstName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lastName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String nickName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Phone phone;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private UserStats userStats;
	
	public UserStats getUserStats() {
		return userStats;
	}

	public void setUserStats(UserStats userStats) {
		this.userStats = userStats;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
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
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", location=" + location + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", nickName=" + nickName + ", password=" + password + ", phone=" + phone + ", userStats=" + userStats
				+ "]";
	}

}
