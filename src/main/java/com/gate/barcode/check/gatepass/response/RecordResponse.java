package com.gate.barcode.check.gatepass.response;

import java.io.Serializable;

import com.gate.barcode.check.gatepass.utilities.TicketStatus;

/**
 * <<This is the response of Records>>
 * @author Munal
 * @version 1.0.0
 * @since , 16 Apr 2018
 */
public class RecordResponse implements Serializable {
	private Long ticketId;
	private Long createdBy;
	private String uniqueId;
	private String price;
	private Long stationMaster;
	private Long stationId;
	private Long gateId;
	private String createdDate;
	private Long modifiedBy;
	private String modifiedDate;
	private Long issuedBy;
	private Long checkedBy;
	private String issuedDate;
	private String checkedDate;
	private TicketStatus ticketStatus;
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Long getStationMaster() {
		return stationMaster;
	}
	public void setStationMaster(Long stationMaster) {
		this.stationMaster = stationMaster;
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
	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	
}
