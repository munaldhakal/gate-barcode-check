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
	private String uniqueId;
	private String price;
	private Long stationMaster;
	private Long stationId;
	private Long gateId;

	// private Long gateId;

	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate")
	private Date createdDate;
	private Long modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	private Long issuedBy;
	private Long checkedBy;
	@Temporal(TemporalType.DATE)
	@Column(name = "issuedDate")
	private Date issuedDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "checkedDate")
	private Date checkedDate;
	@Enumerated(EnumType.STRING)
	private TicketStatus ticketStatus;

	// public Long getGateId() {
	// return gateId;
	// }
	//
	// public void setGateId(Long gateId) {
	// this.gateId = gateId;
	// }

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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public Long getStationMaster() {
		return stationMaster;
	}

	public void setStationMaster(Long stationMaster) {
		this.stationMaster = stationMaster;
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

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(Date checkedDate) {
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
	
}
