package com.gate.barcode.check.gatepass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.utilities.TicketStatus;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllByTicketStatusNot(TicketStatus blocked);

	Ticket findByIdAndTicketStatusNot(Long id, TicketStatus blocked);

	Ticket findByUniqueIdAndTicketStatusNot(String uniqueId, TicketStatus blocked);

	/**
	 *<<Add description here>>
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	List<Ticket> findByCheckedBy(Long userId);

	/**
	 *<<Add description here>>
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	Optional<Ticket> findByStationMaster(Long userId);

	/**
	 *<<Add description here>>
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	List<Ticket> findAllByStationMaster(Long userId);

}
