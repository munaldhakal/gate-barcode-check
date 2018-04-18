package com.gate.barcode.check.gatepass.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.service.spi.ServiceException;
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
import com.gate.barcode.check.gatepass.request.GateEditRequest;
import com.gate.barcode.check.gatepass.response.GateResponse;
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

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void createGate(GateCreationRequest gateCreationRequest) {
		Gate gates = gateRepository.findByTicketChecker(gateCreationRequest.getTicketChecker());
		if (gates != null) {
			throw new ServiceException("Ticket checker with id "+gateCreationRequest.getTicketChecker()+" has been already assigned to some gate.");
		}
		Optional<User> user=userRepository.findById(gateCreationRequest.getTicketChecker());
		if(!user.get().getUserType().equals(UserType.TICKETCHECKER)) {
			throw new ServiceException("You can only assign TicketChecker.");
			
		}
		Gate gate = new Gate();

		gate.setGateName(gateCreationRequest.getGateName());
		gate.setTicketChecker(gateCreationRequest.getTicketChecker());
		gateRepository.save(gate);

	}

	@Transactional
	public List<GateResponse> getAllGates() {
		List<GateResponse> gateResponseList = new ArrayList<GateResponse>();
		List<Gate> gates = gateRepository.findAll();
		gates.stream().forEach(u -> {
			GateResponse gateResponse = new GateResponse();
			gateResponse.setId(u.getId());
			gateResponse.setGateName(u.getGateName());
			gateResponse.setTicketChecker(u.getTicketChecker());
			Optional<User> user = userRepository.findById(u.getTicketChecker());
			gateResponse.setTicketCheckerName(user.get().getName());
			gateResponseList.add(gateResponse);
		});
		;
		return gateResponseList;
	}

	@Transactional
	public GateResponse getGate(Long id) {
		Optional<Gate> gate = gateRepository.findById(id);
		if (!gate.isPresent()) {
			throw new NotFoundException("Gate with id=" + id + " not found.");
		}
		GateResponse gateResponse = new GateResponse();
		gateResponse.setId(gate.get().getId());
		gateResponse.setGateName(gate.get().getGateName());
		gateResponse.setTicketChecker(gate.get().getTicketChecker());
		Optional<User> user = userRepository.findById(gate.get().getTicketChecker());
		gateResponse.setTicketCheckerName(user.get().getName());

		return gateResponse;
	}

	@Transactional
	public void deleteGate(Long id) {
		Optional<Gate> gate = gateRepository.findById(id);
		if (!gate.isPresent()) {
			throw new NotFoundException("Gate with id=" + id + " not found.");
		}
		gateRepository.delete(gate.get());

	}

	public void editgate(GateEditRequest gateEditRequest) {

		Optional<Gate> gate = gateRepository.findById(gateEditRequest.getId());
		if (!gate.isPresent()) {
			throw new NotFoundException("Gate with id=" + gateEditRequest.getId() + " not found.");

		}
		if (gateEditRequest.getGateName() != null)
			gate.get().setGateName(gateEditRequest.getGateName());
		if (gateEditRequest.getTicketChecker() != null)
			gate.get().setTicketChecker(gateEditRequest.getTicketChecker());
		gateRepository.save(gate.get());

	}
}
