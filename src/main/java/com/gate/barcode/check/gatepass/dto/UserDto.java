package com.gate.barcode.check.gatepass.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.gate.barcode.check.gatepass.utilities.UserType;

public class UserDto implements Serializable {
	@NotNull(message="name cannot be null")
	private String name;
	private String address;
	@NotNull(message="phonenumber cannot be null")
	private String phoneNumber;
	@NotNull(message="email cannot be null")
	private String email;
	@NotNull(message="usertype cannot be null")
	private UserType userType;
	@NotNull(message="username cannot be null")
	private String username;
	@NotNull(message="password cannot be null")
	private String password;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
