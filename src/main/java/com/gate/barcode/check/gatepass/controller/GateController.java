package com.gate.barcode.check.gatepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.request.GateCreationRequest;
import com.gate.barcode.check.gatepass.service.CommonService;
import com.gate.barcode.check.gatepass.service.GateService;

import io.swagger.annotations.ApiOperation;

/**
 * <<This is the controller to handle the request comming to gate api>>
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@RestController
@RequestMapping("/api/v1/gates")
public class GateController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GateService gateService;
	
	@ApiOperation(value="Create gate", notes="create gate")
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Object> createGate(@RequestBody GateCreationRequest gateCreationRequest,@RequestHeader Long userId){
		commonService.checkUserType(userId);   //checks whether the user is an ADMIN or not.
		gateService.createGate(gateCreationRequest);     
		return new ResponseEntity<Object>(HttpStatus.CREATED);
		
	}

}
