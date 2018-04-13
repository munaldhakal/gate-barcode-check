package com.gate.barcode.check.gatepass.request;

import java.io.Serializable;

import com.gate.barcode.check.gatepass.utilities.TicketStatus;

/**
 * <<This is the request to check the bar code>>
 * @author Munal
 * @version 1.0.0
 * @since , 13 Apr 2018
 */
public class BarCodeCheckRequest implements Serializable {
	private TicketStatus status;

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}
	
}
