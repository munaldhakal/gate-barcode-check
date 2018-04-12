package com.gate.barcode.check.gatepass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.repository.StationRepository;

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
}
