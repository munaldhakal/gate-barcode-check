package com.gate.barcode.check.gatepass.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gate.barcode.check.gatepass.utilities.TicketStatus;

@SuppressWarnings("serial")
@Entity
public class Ticket implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String barcode;
	private Long createdBy;
	private String createrName;
	private String uniqueId;
	private String price;
	private Long stationMasterId;
	private String stationMasterName;
	private Long stationId;
	private String stationName;
	private Long gateId;
	private String gateName;
	//@Temporal(TemporalType.DATE)
	@Column(name = "createdDate")
	private String createdDate;
	private Long modifiedBy;
	private String modifierName;
	//@Temporal(TemporalType.DATE)
	@Column(name = "modifiedDate")
	private String modifiedDate;
	private Long issuedBy;
	private String issuerName;
	private Long checkedBy;
	private String checkerName;
	//@Temporal(TemporalType.DATE)
	@Column(name = "issuedDate")
	private String issuedDate;
	//@Temporal(TemporalType.DATE)
	@Column(name = "checkedDate")
	private String checkedDate;
	@Enumerated(EnumType.STRING)
	private TicketStatus ticketStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public Long getStationMasterId() {
		return stationMasterId;
	}

	public void setStationMasterId(Long stationMasterId) {
		this.stationMasterId = stationMasterId;
	}

	public String getStationMasterName() {
		return stationMasterName;
	}

	public void setStationMasterName(String stationMasterName) {
		this.stationMasterName = stationMasterName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getGateName() {
		return gateName;
	}

	public void setGateName(String gateName) {
		this.gateName = gateName;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public Long getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Long issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Long getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(Long checkedBy) {
		this.checkedBy = checkedBy;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(String checkedDate) {
		this.checkedDate = checkedDate;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getGateId() {
		return gateId;
	}

	public void setGateId(Long gateId) {
		this.gateId = gateId;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	
}
