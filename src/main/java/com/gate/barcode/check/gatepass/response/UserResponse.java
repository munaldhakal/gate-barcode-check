package com.gate.barcode.check.gatepass.response;

import java.io.Serializable;

import com.gate.barcode.check.gatepass.utilities.UserType;

public class UserResponse implements Serializable {
	private Long id;
	private String name;
	private String address;
	private String phoneNumber;
	private String email;
	private UserType userType;
	private Long createdBy;
	private Long editedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(Long editedBy) {
		this.editedBy = editedBy;
	}

}
