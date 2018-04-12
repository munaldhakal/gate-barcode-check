package com.gate.barcode.check.gatepass.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.dto.LoginDto;
import com.gate.barcode.check.gatepass.model.Login;
import com.gate.barcode.check.gatepass.repository.LoginRepository;
import com.gate.barcode.check.gatepass.utilities.BCrypt;
import com.gate.barcode.check.gatepass.utilities.LoginStatus;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private UserService userService;

	/**
	 * This method logins user according to the username and password
	 * @param loginDto
	 * @return Map<Object,Object>
	 * @author Munal
	 * @since 11/04/2018
	 */
	@Transactional
	public Map<Object, Object> login(LoginDto loginDto) {
		Login login = loginRepository.findByUsername(loginDto.getUsername());
		if (login == null)
			throw new ServiceException(
					"No login found for username :" + loginDto.getUsername());
		if (BCrypt.checkpw(loginDto.getPassword(), login.getPassword())) {
			login.setLastLogin(new Date());
			login.setLoginStatus(LoginStatus.LOGGEDIN);
			loginRepository.save(login);
		}
		Map<Object, Object> response = new HashMap<>();
		response.put("username", login.getUsername());
		response.put("user", userService.getUser(login.getUserId()));
		return response;
	}

	/**
	 * This method is used to log out the user by userId
	 * @param userId
	 * @author Munal
	 * @since 11/04/2018
	 */
	public void logout(Long userId) {
		Optional<Login> login = loginRepository.findById(userId);
		if (!login.isPresent())
			throw new ServiceException("No login found of Id: " + userId);
		login.get().setLoginStatus(LoginStatus.LOGGEDOUT);
		loginRepository.save(login.get());
	}
}
