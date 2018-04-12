package com.gate.barcode.check.gatepass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.utilities.TicketStatus;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	//List<Ticket> findAllAndTicketStatusNot(TicketStatus blocked);

	List<Ticket> findAllByTicketStatusNot(TicketStatus blocked);

}
