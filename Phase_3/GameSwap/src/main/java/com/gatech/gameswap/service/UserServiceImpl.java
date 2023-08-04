package com.gatech.gameswap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.User;
import com.gatech.gameswap.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public Boolean createUser(User user) {
		return userRepository.createUser(user);	
	
	}

	@Override
	public Boolean checkEmailExists(String email) {		
		return userRepository.checkEmailExists(email);
	}

	@Override
	public Boolean checkPhoneExists(String phone) {
		return userRepository.checkPhoneExists(phone);
	}

	@Override
	public List<Location> getLocations() {
		return userRepository.getLocations();
	}

	@Override
	public String authenticateUser(String email, String password) {		
		return userRepository.authenticateUser(email, password);
	}

	@Override
	public User getUser(String email) {		
		return userRepository.getUser(email);
	}

	@Override
	public Double getRating(String email) {
		return userRepository.getRating(email);
	}

	@Override
	public Boolean updateUser(User user) {
		return userRepository.updateUser(user);
	}
}
