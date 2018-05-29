package com.gate.barcode.check.gatepass.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.utilities.TicketStatus;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllByTicketStatusNot(TicketStatus blocked);

	Ticket findByIdAndTicketStatusNot(Long id, TicketStatus blocked);

	Ticket findByIdAndTicketStatus(Long id, TicketStatus unverified);

	Ticket findByUniqueIdAndTicketStatusNot(String uniqueId, TicketStatus blocked);

	/**
	 * <<Add description here>>
	 * 
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	List<Ticket> findByCheckedBy(Long userId);

	/**
	 * <<Add description here>>
	 * 
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	Optional<Ticket> findByStationMasterId(Long userId);

	/**
	 * <<Add description here>>
	 * 
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	List<Ticket> findAllByStationMasterId(Long userId);

	/**
	 * <<Add description here>>
	 * 
	 * @param userId
	 * @param unverified
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	List<Ticket> findAllByStationMasterIdAndTicketStatus(Long userId, TicketStatus unverified);

	/**
	 * <<Add description here>>
	 * 
	 * @param unverified
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	List<Ticket> findAllByTicketStatus(TicketStatus unverified);
	
	@Query("SELECT MAX(id) FROM Ticket")
	Long getLastIndex();

	Page<Ticket> findByStationMasterId(Long userId, Pageable pageable);

	Page<Ticket> findByCheckedBy(Long userId, Pageable pageable);

	Page<Ticket> findAllByTicketStatus(TicketStatus unverified, Pageable pageable);

	Page<Ticket> findAllByStationMasterIdAndTicketStatus(Long userId, TicketStatus unverified, Pageable pageable);

	Page<Ticket> findAllByIdAndTicketStatus(Long search, TicketStatus unverified, Pageable pageable);

	Page<Ticket> findAllByPriceAndTicketStatus(String search, TicketStatus unverified, Pageable pageable);

	Page<Ticket> findAllByUniqueIdAndTicketStatus(String search, TicketStatus unverified, Pageable pageable);

	Page<Ticket> findAllByIdAndStationMasterIdAndTicketStatus(Long search, Long userId, TicketStatus unverified,
			Pageable pageable);

	Page<Ticket> findAllByPriceAndStationMasterIdAndTicketStatus(String search, Long userId, TicketStatus unverified,
			Pageable pageable);

	Page<Ticket> findAllByUniqueIdAndStationMasterIdAndTicketStatus(String search, Long userId, TicketStatus unverified,
			Pageable pageable);

	Page<Ticket> findByIdAndCheckedBy(Long valueOf, Long userId, Pageable pageable);

	Page<Ticket> findByPriceAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByTicketStatusAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByStationMasterNameAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByStationNameAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByGateNameAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByCreaterNameAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByCreatedDateAndCheckedBy(String string, Long userId, Pageable pageable);

	Page<Ticket> findByIssuerNameAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByIssuedDateAndCheckedBy(String date, Long userId, Pageable pageable);

	Page<Ticket> findByCheckedDateAndCheckedBy(String date, Long userId, Pageable pageable);

	Page<Ticket> findByCheckerNameAndCheckedBy(String search, Long userId, Pageable pageable);

	Page<Ticket> findByIdAndStationMasterId(Long valueOf, Long userId, Pageable pageable);

	Page<Ticket> findByPriceAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByTicketStatusAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByStationNameAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByStationMasterNameAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByGateNameAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByCreaterNameAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByCreatedDateAndStationMasterId(String date, Long userId, Pageable pageable);

	Page<Ticket> findByIssuedDateAndStationMasterId(String string, Long userId, Pageable pageable);

	Page<Ticket> findByIssuerNameAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByCheckerNameAndStationMasterId(String search, Long userId, Pageable pageable);

	Page<Ticket> findByCheckedDateAndStationMasterId(String date, Long userId, Pageable pageable);

	Page<Ticket> findById(Long valueOf, Pageable pageable);

	Page<Ticket> findByPrice(String search, Pageable pageable);

	Page<Ticket> findByTicketStatus(String search, Pageable pageable);

	Page<Ticket> findByStationName(String search, Pageable pageable);

	Page<Ticket> findByStationMasterName(String search, Pageable pageable);

	Page<Ticket> findByGateName(String search, Pageable pageable);

	Page<Ticket> findByCreaterName(String search, Pageable pageable);

	Page<Ticket> findByCreatedDate(String date, Pageable pageable);

	Page<Ticket> findByIssuerName(String search, Pageable pageable);

	Page<Ticket> findByIssuedDate(String date, Pageable pageable);

	Page<Ticket> findByCheckerName(String search, Pageable pageable);

	Page<Ticket> findByCheckedDate(String date, Pageable pageable);

}
