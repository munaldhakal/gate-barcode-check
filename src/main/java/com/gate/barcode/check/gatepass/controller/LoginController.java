package com.gate.barcode.check.gatepass.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.dto.LoginDto;
import com.gate.barcode.check.gatepass.service.LoginService;

@RestController
@RequestMapping("/api/v1/logins")
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<Object>login(@RequestBody LoginDto loginDto){
		Map<Object,Object> response = loginService.login(loginDto);
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	public ResponseEntity<Object>logout(@RequestHeader Long userId){
		loginService.logout(userId);
		return new ResponseEntity<Object>("Logged Out Successfully",HttpStatus.OK);
	}
}
