package com.gate.barcode.check.gatepass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.exception.AlreadyExistException;
import com.gate.barcode.check.gatepass.exception.NotAuthorizedException;
import com.gate.barcode.check.gatepass.exception.NotFoundException;
import com.gate.barcode.check.gatepass.model.Gate;
import com.gate.barcode.check.gatepass.model.User;
import com.gate.barcode.check.gatepass.repository.GateRepository;
import com.gate.barcode.check.gatepass.repository.UserRepository;
import com.gate.barcode.check.gatepass.request.GateCreationRequest;
import com.gate.barcode.check.gatepass.utilities.UserType;

/**
 * <<This is the service to handle gate operations>>
 * 
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@Service
public class GateService {
	@Autowired
	private GateRepository gateRepository;

	public void createGate(GateCreationRequest gateCreationRequest) {
		Gate gate = gateRepository.findByGateName(gateCreationRequest.getGateName());
		if (gate != null) {
			throw new AlreadyExistException("Gate with name" + gateCreationRequest.getGateName() + " already exist.");
		}
		gate = new Gate();

		gate.setGateName(gateCreationRequest.getGateName());
		gate.setTicketChecker(gateCreationRequest.getTicketChecker());
		gateRepository.save(gate);

	}
}
