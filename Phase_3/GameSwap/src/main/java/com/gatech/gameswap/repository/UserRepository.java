package com.gatech.gameswap.repository;

import java.util.List;

import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.User;

public interface UserRepository {

	Boolean createUser(User user);
	Boolean updateUser(User user);
	String authenticateUser(String email, String password);
	User getUser(String email);
	Boolean checkEmailExists(String email);
	Boolean checkPhoneExists(String phone);
	Double getRating(String email);
	List<Location> getLocations();

}
