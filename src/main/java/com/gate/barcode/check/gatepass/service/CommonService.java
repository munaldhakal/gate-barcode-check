package com.gate.barcode.check.gatepass.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.model.Login;
import com.gate.barcode.check.gatepass.repository.LoginRepository;
import com.gate.barcode.check.gatepass.utilities.LoginStatus;

@Service
public class CommonService {
	@Autowired
	private LoginRepository loginRepository;
	
	public Boolean checkLoginStatus(Long loginId) {
		Login login = loginRepository.getOne(loginId);
		if(login==null)
			throw new ServiceException("Sorry no Login Found of Id :"+loginId);
		if(login.getLoginStatus().equals(LoginStatus.LOGGEDOUT))
			return false;
		return true;
	}
}
