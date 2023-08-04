package com.gatech.gameswap.service;

import java.util.List;

import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.Phone;
import com.gatech.gameswap.model.User;

public interface UserService {

	Boolean createUser(User user);
	String authenticateUser(String email, String password);
	User getUser(String email);
	Boolean updateUser(User user);
	Boolean checkEmailExists(String email);
	Boolean checkPhoneExists(String phone);
	List<Location> getLocations();
	Double getRating(String email);
}
