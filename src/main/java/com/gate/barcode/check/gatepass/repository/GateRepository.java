package com.gate.barcode.check.gatepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.Gate;

/**
 * <<This is the repository to handle the gate request>>
 * 
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {

	Gate findByGateName(String gatename);

	Gate findByTicketChecker(Long ticketChecker);

}
