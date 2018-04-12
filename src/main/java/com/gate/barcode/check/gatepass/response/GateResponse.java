package com.gate.barcode.check.gatepass.response;

public class GateResponse {
	
	private Long id;
	private String gateName;
	private Long ticketChecker;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
