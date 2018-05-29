package com.gate.barcode.check.gatepass.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.dto.UserDto;
import com.gate.barcode.check.gatepass.model.Login;
import com.gate.barcode.check.gatepass.model.User;
import com.gate.barcode.check.gatepass.repository.LoginRepository;
import com.gate.barcode.check.gatepass.repository.UserRepository;
import com.gate.barcode.check.gatepass.request.UserEditRequest;
import com.gate.barcode.check.gatepass.response.UserResponse;
import com.gate.barcode.check.gatepass.utilities.BCrypt;
import com.gate.barcode.check.gatepass.utilities.LoginStatus;
import com.gate.barcode.check.gatepass.utilities.UserType;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginRepository loginRepository;

	/**
	 * This is the method to create the user
	 * 
	 * @param userId
	 * @param userDto
	 * @author Munal
	 * @since 11/04/2018
	 */
	@Transactional
	public void createUser(Long userId, UserDto userDto) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent())
			throw new ServiceException("No user found of userid :" + userId);
		if (!user.get().getUserType().equals(UserType.ADMIN)) {
			throw new ServiceException("Sorry you are not authorized");
		}
		User toCreate = new User();
		toCreate.setAddress(userDto.getAddress());
		toCreate.setCreatedBy(user.get().getId());
		toCreate.setEmail(userDto.getEmail());
		toCreate.setName(userDto.getName());
		toCreate.setPhoneNumber(userDto.getPhoneNumber());
		toCreate.setUserType(userDto.getUserType());
		Login createLogin = loginRepository.findByUsername(userDto.getUsername());
		if(createLogin!=null)
			throw new ServiceException("Username already exists. Please try with different username");
		createLogin= new Login();
		toCreate = userRepository.save(toCreate);
		createLogin.setUsername(userDto.getUsername());
		createLogin.setUserId(toCreate.getId());
		createLogin.setLoginStatus(LoginStatus.LOGGEDOUT);
		createLogin.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
		loginRepository.save(createLogin);
	}
	/**
	 * This method edits a user
	 * @param userId
	 * @param request
	 * @author Munal
	 * @since 11/04/2018
	 */
	@Transactional
	public void editUser(Long userId, UserEditRequest request) {
		Optional<User> admin = userRepository.findById(userId);
		if(!admin.isPresent())
			throw new ServiceException("Sorry your Id is wrong");
		if(!admin.get().getUserType().equals(UserType.ADMIN))
			throw new ServiceException("Sorry you are not authorized.");
		Optional<User> toEdit = userRepository.findById(request.getId());
		if(!toEdit.isPresent())
			throw new ServiceException("No user found to edit");
		if(request.getAddress()!=null)
			toEdit.get().setAddress(request.getAddress());
		if(request.getEmail()!=null)
			toEdit.get().setEmail(request.getEmail());
		if(request.getName()!=null)
			toEdit.get().setName(request.getName());
		if(request.getPhoneNumber()!=null)
			toEdit.get().setPhoneNumber(request.getPhoneNumber());
		if(request.getUserType()!=null)
			toEdit.get().setUserType(request.getUserType());
		toEdit.get().setEditedBy(admin.get().getId());
		userRepository.save(toEdit.get());
	}
	/**
	 * This method deletes a user by id
	 * @param userId
	 * @param id
	 * @author Munal
	 * @since 11/04/2018
	 */
	@Transactional
	public void deleteUser(Long userId, Long id) {
		Optional<User> admin = userRepository.findById(userId);
		if(!admin.isPresent())
			throw new ServiceException("Sorry your Id is wrong");
		if(!admin.get().getUserType().equals(UserType.ADMIN))
			throw new ServiceException("Sorry you are not authorized.");
		Optional<User> toDelete = userRepository.findById(id);
		if(!toDelete.isPresent())
			throw new ServiceException("No user found to delete");
		if(admin.get().getId()==toDelete.get().getId())
			throw new ServiceException("Sorry you cannot delete yourself");
		userRepository.delete(toDelete.get());
	}
	/**
	 * This method returns user according to id
	 * @param id
	 * @return UserResponse
	 * @author Munal
	 * @since 11/04/2018
	 */
	@Transactional
	public UserResponse getUser(Long id) {
		Optional<User> user =userRepository.findById(id);
		if(!user.isPresent())
			throw new ServiceException("Sorry no user found");
		return getUserObj(user.get());
	}
	/**
	 * This method returns user to get =Userr and getAllUsers methods
	 * @param user
	 * @return UserResponse
	 * @author Munal
	 * @since 11/04/2018
	 */
	private UserResponse getUserObj(User user) {
		UserResponse response =new UserResponse();
		if(user.getAddress()!=null)
			response.setAddress(user.getAddress());
		response.setCreatedBy(user.getCreatedBy());
		response.setEditedBy(user.getEditedBy());
		response.setEmail(user.getEmail());
		response.setId(user.getId());
		response.setName(user.getName());
		response.setPhoneNumber(user.getPhoneNumber());
		response.setUserType(user.getUserType());
		return response;
	}
	/**
	 * This method returns list of all users
	 * @return List<UserResponse>
	 * @author Munal
	 * @param userType 
	 * @since 11/04/2018
	 */
	@Transactional
	public List<UserResponse> getAllUsers(Long userId,UserType userType) {
		List<User> user=null;
		if(userType==null)
			user = userRepository.findAll();
		else {
			user=userRepository.findAllByUserType(userType);
		}
		if(user==null)
			throw new ServiceException("Sorry no user found");
		List<UserResponse> response = new ArrayList<>();
		for(User u: user) {
			if(u.getId()==userId)
				continue;
			response.add(getUserObj(u));
		}
		return response;
	}
}
