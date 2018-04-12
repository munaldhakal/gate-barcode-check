package com.gate.barcode.check.gatepass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.exception.AlreadyExistException;
import com.gate.barcode.check.gatepass.model.Station;
import com.gate.barcode.check.gatepass.repository.StationRepository;
import com.gate.barcode.check.gatepass.request.StationCreationRequest;

/**
 * <<This is the service to handle gate operations>>
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@Service
public class StationService {
	@Autowired
	private StationRepository stationRepository;

	public void createStation(StationCreationRequest stationCreationRequest) {
		Station station=stationRepository.findByStationName(stationCreationRequest.getStationName());
		if(station!=null) {
			
			throw new AlreadyExistException("Station with name "+stationCreationRequest.getStationName()+" already exist.");
		}
		station =new Station();
		station.setStationName(stationCreationRequest.getStationName());
		station.setStationMaster(stationCreationRequest.getStationMaster());
		stationRepository.save(station);
		
		
	}
}
