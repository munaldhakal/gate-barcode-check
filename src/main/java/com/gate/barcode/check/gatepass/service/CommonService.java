package com.gate.barcode.check.gatepass.service;

import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.exception.NotAuthorizedException;
import com.gate.barcode.check.gatepass.exception.NotFoundException;
import com.gate.barcode.check.gatepass.model.Gate;
import com.gate.barcode.check.gatepass.model.Login;
import com.gate.barcode.check.gatepass.model.User;
import com.gate.barcode.check.gatepass.repository.GateRepository;
import com.gate.barcode.check.gatepass.repository.LoginRepository;
import com.gate.barcode.check.gatepass.repository.UserRepository;
import com.gate.barcode.check.gatepass.request.GateCreationRequest;
import com.gate.barcode.check.gatepass.utilities.LoginStatus;
import com.gate.barcode.check.gatepass.utilities.UserType;

@Service
public class CommonService {
	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private UserRepository userRepository;

	public Boolean checkLoginStatus(Long loginId) {
		Optional<Login> login = loginRepository.findById(loginId);
		if (!login.isPresent())
			throw new ServiceException("Sorry no Login Found of Id :" + loginId);
		if (login.get().getLoginStatus().equals(LoginStatus.LOGGEDOUT))
			return false;
		return true;
	}

	public void checkUserType(Long userId) {
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new NotFoundException("user with id=" + userId + " not found.");
		}
		if (!user.get().getUserType().equals(UserType.ADMIN)) {
			throw new NotAuthorizedException("Not enough privilege!");
		}

	}

}
