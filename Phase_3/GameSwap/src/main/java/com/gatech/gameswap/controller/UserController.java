package com.gatech.gameswap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.User;
import com.gatech.gameswap.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("create a new user with given details")
	public ResponseEntity<Boolean> createUser(@RequestBody User user) {
		
		Boolean success = false;
		try{
			success = userService.createUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(success, success ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(method = RequestMethod.GET, path="/authenticate")
	@ApiOperation("authenticate user during login")
	public ResponseEntity<String> authenticateUser(@RequestParam String email, @RequestParam String password) {
		
		String success = null;
		try{
			success = userService.authenticateUser(email, password);
		}catch(Exception e) {
			e.printStackTrace();
			success = null;
		}
		return new ResponseEntity<String>(success, success!=null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation("get details for user with given email")
	public ResponseEntity<User> getUser(@RequestParam String email) {
		
		User user = null;
		try{
			user = userService.getUser(email);
		}catch(Exception e) {
			e.printStackTrace();
			user = null;
		}
		return new ResponseEntity<User>(user, user!=null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("update an existing user with given details")
	public ResponseEntity<String> updateUser(@RequestBody User user) {
		Boolean success = null;
		try{
			success = userService.updateUser(user);
		}catch(Exception e) {
			e.printStackTrace();
			success = null;
		}
		return new ResponseEntity<String>(String.valueOf(success), success != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/exists/email")
	@ApiOperation("check if user already exists with given email")
	public ResponseEntity<String> checkEmailExists(@RequestParam("email") String email) {
		Boolean exists = true;
		try{
			exists = userService.checkEmailExists(email);
		}catch(Exception e) {
			e.printStackTrace();
			exists = null;
		}
		return new ResponseEntity<String>(String.valueOf(exists), exists != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/exists/phone")
	@ApiOperation("check if user already exists with given phone number")
	public ResponseEntity<String> checkPhoneExists(@RequestParam("phone") String phone) {
		Boolean exists = true;
		try{
			exists = userService.checkPhoneExists(phone);
		}catch(Exception e) {
			e.printStackTrace();
			exists = null;
		}
		return new ResponseEntity<String>(String.valueOf(exists), exists != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/postalcodes")
	@ApiOperation("get list of locations during user registration")
	public ResponseEntity<List<Location>> getPostalCodes() {
		List<Location> locations = null;
		try{
			locations = userService.getLocations();
		}catch(Exception e) {
			e.printStackTrace();
			locations = null;
		}
		return new ResponseEntity<List<Location>>(locations, locations != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/*
	@RequestMapping(method = RequestMethod.GET, path = "/rating")
	@ApiOperation("get rating for user with given email")
	public ResponseEntity<Double> getRating(@RequestParam("email") String email) {
		Double rating = null;
		try{
			rating = userService.getRating(email);
		}catch(Exception e) {
			e.printStackTrace();
			rating = null;
		}
		return new ResponseEntity<Double>(rating, rating != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}*/
		
}
