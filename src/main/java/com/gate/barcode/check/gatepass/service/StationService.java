package com.gate.barcode.check.gatepass.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.exception.AlreadyExistException;
import com.gate.barcode.check.gatepass.exception.NotFoundException;
import com.gate.barcode.check.gatepass.model.Station;
import com.gate.barcode.check.gatepass.model.User;
import com.gate.barcode.check.gatepass.repository.StationRepository;
import com.gate.barcode.check.gatepass.repository.UserRepository;
import com.gate.barcode.check.gatepass.request.StationCreationRequest;
import com.gate.barcode.check.gatepass.request.StationEditRequest;
import com.gate.barcode.check.gatepass.response.StationResponse;
import com.gate.barcode.check.gatepass.utilities.UserType;

/**
 * <<This is the service to handle gate operations>>
 * 
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@Service
public class StationService {
	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void createStation(StationCreationRequest stationCreationRequest) {
		Station stations = stationRepository.findByStationMaster(stationCreationRequest.getStationMaster());
		if (stations != null) {
			throw new AlreadyExistException("Station Master with id " + stationCreationRequest.getStationMaster()
					+ " is already asigned to some station");
		}

		Optional<User> user = userRepository.findById(stationCreationRequest.getStationMaster());
		if (!user.get().getUserType().equals(UserType.STATIONMASTER)) {
			throw new ServiceException("You can only assign StationMaster");
		}
		Station station = new Station();
		station.setStationName(stationCreationRequest.getStationName());
		station.setStationMaster(stationCreationRequest.getStationMaster());
		stationRepository.save(station);

	}

	@Transactional
	public List<StationResponse> getAllStation() {
		List<StationResponse> stationResponseList = new ArrayList<StationResponse>();
		List<Station> stations = stationRepository.findAll();
		if(stations.size()==0)
			throw new ServiceException("Sorry No Station found");
		stations.stream().forEach(u -> {
			StationResponse stationResponse = new StationResponse();
			stationResponse.setId(u.getId());
			stationResponse.setStationMaster(u.getStationMaster());
			stationResponse.setStationName(u.getStationName());
			Optional<User> user = userRepository.findById(u.getStationMaster());
			if(user.isPresent())
			stationResponse.setStationMasterName(user.get().getName());
			stationResponseList.add(stationResponse);
		});
		return stationResponseList;
	}

	@Transactional
	public StationResponse getStation(Long id) {
		Optional<Station> station = stationRepository.findById(id);
		if (!station.isPresent()) {
			throw new NotFoundException("Station with id " + id + " not found.");

		}
		StationResponse stationResponse = new StationResponse();
		stationResponse.setId(id);
		stationResponse.setStationMaster(station.get().getStationMaster());
		stationResponse.setStationName(station.get().getStationName());
		Optional<User> user = userRepository.findById(station.get().getStationMaster());
		if(user.isPresent())
		stationResponse.setStationMasterName(user.get().getName());
		return stationResponse;
	}

	@Transactional
	public void deleteStation(Long id) {
		Optional<Station> station = stationRepository.findById(id);
		if (!station.isPresent()) {
			throw new NotFoundException("Station with id " + id + " not found.");

		}
		stationRepository.delete(station.get());

	}

	@Transactional
	public void editStation(StationEditRequest stationEditRequest) {
		Optional<Station> station = stationRepository.findById(stationEditRequest.getId());
		if (!station.isPresent()) {
			throw new NotFoundException("Station with id " + stationEditRequest.getId() + " not found.");
		}
		if (stationEditRequest.getStationMaster() != null)
			station.get().setStationMaster(stationEditRequest.getStationMaster());
		if (stationEditRequest.getStationName() != null)
			station.get().setStationName(stationEditRequest.getStationName());
		stationRepository.save(station.get());

	}
}
