package com.gate.barcode.check.gatepass.request;

public class GateCreationRequest {

	private String gateName;
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
