package com.gate.barcode.check.gatepass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.request.StationCreationRequest;
import com.gate.barcode.check.gatepass.service.CommonService;
import com.gate.barcode.check.gatepass.service.StationService;

import io.swagger.annotations.ApiOperation;

/**
 * <<This is the station controller that handles the request comming to this api>>
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@RestController
@RequestMapping("/api/v1/stations")
public class StationController {
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StationService stationService;
	
	@ApiOperation(value="Create station", notes="Create station")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Object> createStation(@RequestBody StationCreationRequest stationCreationRequest, @PathVariable Long userId){
		commonService.checkUserType(userId);    //checks whether the user is ADMIN or not.
		stationService.createStation(stationCreationRequest);
		return new ResponseEntity<Object>(HttpStatus.CREATED);
		
	}

}
