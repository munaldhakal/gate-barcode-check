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

import com.gate.barcode.check.gatepass.request.GateCreationRequest;
import com.gate.barcode.check.gatepass.request.GateEditRequest;
import com.gate.barcode.check.gatepass.response.GateResponse;
import com.gate.barcode.check.gatepass.service.CommonService;
import com.gate.barcode.check.gatepass.service.GateService;

import io.swagger.annotations.ApiOperation;

/**
 * <<This is the controller to handle the request comming to gate api>>
 * 
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

	@ApiOperation(value = "Create gate", notes = "create gate")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createGate(@RequestBody GateCreationRequest gateCreationRequest,
			@RequestHeader Long userId) {
		commonService.checkUserType(userId); // checks whether the user is an ADMIN or not.
		gateService.createGate(gateCreationRequest);
		return new ResponseEntity<Object>("Successfully Created", HttpStatus.CREATED);

	}

	@ApiOperation(value = "Get all gates", notes = "get all gates")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getAllGates() {
	//	commonService.checkUserType(userId); // checks whether the user is an ADMIN or not.
		List<GateResponse> gateResponseList = gateService.getAllGates();
		return new ResponseEntity<Object>(gateResponseList, HttpStatus.OK);
	}

	@ApiOperation(value = "Get gate", notes = "get gate")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getGate(@RequestHeader Long userId, @PathVariable Long id) {
		commonService.checkUserType(userId); // checks whether the user is an ADMIN or not.
		GateResponse gateResponse = gateService.getGate(id);
		return new ResponseEntity<Object>(gateResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete gate", notes = "Delete Gates")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteGate(@PathVariable Long id, @RequestHeader Long userId) {
		commonService.checkUserType(userId); // checks whether the user is an ADMIN or not.
		gateService.deleteGate(id);
		return new ResponseEntity<Object>("Successfully Deleted", HttpStatus.OK);

	}

	@ApiOperation(value = "Edit gate", notes = "Edit gates")
	@RequestMapping( method = RequestMethod.PUT)
	public ResponseEntity<Object> editGate(@RequestBody GateEditRequest gateEditRequest, @RequestHeader Long userId) {
		commonService.checkUserType(userId); // checks whether the user is an ADMIN or not.
		gateService.editgate(gateEditRequest);
		return new ResponseEntity<Object>("Edited Successfuly", HttpStatus.OK);

	}

}
