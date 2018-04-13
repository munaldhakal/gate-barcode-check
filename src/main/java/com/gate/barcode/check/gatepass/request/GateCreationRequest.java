package com.gate.barcode.check.gatepass.request;

import javax.validation.constraints.NotNull;

public class GateCreationRequest {

	@NotNull(message = "gatename cannot be null")
	private String gateName;
	@NotNull(message = "ticketChecker cannot be null")
	private Long ticketChecker;
	public String getGateName() {
		return gateName;
	}
	public void setGateName(String gateName) {
		this.gateName = gateName;
	}
	public Long getTicketChecker() {
		return ticketChecker;
	}
	public void setTicketChecker(Long ticketChecker) {
		this.ticketChecker = ticketChecker;
	}

}
