package com.gate.barcode.check.gatepass.request;

import javax.validation.constraints.NotNull;

public class GateEditRequest {
	
	@NotNull(message="Id to edit cannot be null")
	private long id;
	private String gateName;
	private Long ticketChecker;
	
	public Long getTicketChecker() {
		return ticketChecker;
	}

	public void setTicketChecker(Long ticketChecker) {
		this.ticketChecker = ticketChecker;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public String getGateName() {
		return gateName;
	}

	public void setGateName(String gateName) {
		this.gateName = gateName;
	}
	

}
