package com.gate.barcode.check.gatepass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.dto.UserDto;
import com.gate.barcode.check.gatepass.request.UserEditRequest;
import com.gate.barcode.check.gatepass.response.UserResponse;
import com.gate.barcode.check.gatepass.service.UserService;
import com.gate.barcode.check.gatepass.utilities.UserType;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createUsers(@RequestHeader Long userId, @RequestBody UserDto userDto) {
		userService.createUser(userId, userDto);
		return new ResponseEntity<Object>("Successfully Created User", HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Object> editUsers(@RequestHeader Long userId, @RequestBody UserEditRequest request) {
		userService.editUser(userId, request);
		return new ResponseEntity<Object>("Successfully Edited User", HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@RequestHeader Long userId, @PathVariable Long id) {
		userService.deleteUser(userId, id);
		return new ResponseEntity<Object>("Successfully Deleted User", HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUser(@PathVariable Long id) {
		UserResponse response = userService.getUser(id);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getAllUsers(@RequestHeader Long userId,@RequestParam(required=false)UserType userType) {
		List<UserResponse> response = userService.getAllUsers(userId,userType);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
