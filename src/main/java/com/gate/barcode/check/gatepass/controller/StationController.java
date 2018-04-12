package com.gate.barcode.check.gatepass.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.request.StationCreationRequest;
import com.gate.barcode.check.gatepass.request.StationEditRequest;
import com.gate.barcode.check.gatepass.response.StationResponse;
import com.gate.barcode.check.gatepass.service.CommonService;
import com.gate.barcode.check.gatepass.service.StationService;

import io.swagger.annotations.ApiOperation;

/**
 * <<This is the station controller that handles the request comming to this
 * api>>
 * 
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

	@ApiOperation(value = "Create station", notes = "Create station")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createStation(@RequestBody StationCreationRequest stationCreationRequest,
			@RequestHeader Long userId) {
		commonService.checkUserType(userId); // checks whether the user is ADMIN or not.
		stationService.createStation(stationCreationRequest);
		return new ResponseEntity<Object>("Station Created Successfully!", HttpStatus.CREATED);

	}

	@ApiOperation(value = "Get all Station", notes = "Get all stations")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getAllStations(@RequestHeader Long userId) {
		commonService.checkUserType(userId);
		List<StationResponse> stationResponseList = stationService.getAllStation();
		return new ResponseEntity<Object>(stationResponseList, HttpStatus.OK);

	}

	@ApiOperation(value = "get station", notes = "get station")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getStation(@RequestHeader Long userId, @PathVariable Long id) {
		commonService.checkUserType(id);
		StationResponse stationResponse = stationService.getStation(id);
		return new ResponseEntity<Object>(stationResponse, HttpStatus.OK);

	}

	@ApiOperation(value = "Delete Station", notes = "Delete station")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteStation(@RequestHeader Long userId, @PathVariable Long id) {
		commonService.checkUserType(userId);
		stationService.deleteStation(id);
		return new ResponseEntity<Object>("Station Deleted Successfully", HttpStatus.OK);

	}
	
	@ApiOperation(value="Edit station", notes="Edit station")
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<Object> editStation(@RequestHeader Long userId, @RequestBody StationEditRequest stationEditRequest){
		commonService.checkUserType(userId);
		stationService.editStation(stationEditRequest);
		return new ResponseEntity<Object>("Edited Successfully",HttpStatus.OK);
		
	}

}
