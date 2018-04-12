package com.gate.barcode.check.gatepass.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * <<This is the class to send the assign request>>
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
public class AssignTicketsRequest implements Serializable {
	@NotNull(message = "Start id cannot be null")
	private Long idFrom;
	@NotNull(message = "End id cannot be null")
	private Long idTo;
	private String price;
	@NotNull(message = "StationMaster ID cannot be null")
	private Long stationMaster;

	public Long getIdFrom() {
		return idFrom;
	}

	public void setIdFrom(Long idFrom) {
		this.idFrom = idFrom;
	}

	public Long getIdTo() {
		return idTo;
	}

	public void setIdTo(Long idTo) {
		this.idTo = idTo;
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

}
