package com.gate.barcode.check.gatepass.request;

import javax.validation.constraints.NotNull;

public class StationCreationRequest {
	@NotNull(message = "stationName  should not be null")
	private String stationName;
	@NotNull(message = "stationMaster  should not be null")
	private Long stationMaster;

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getStationMaster() {
		return stationMaster;
	}

	public void setStationMaster(Long stationMaster) {
		this.stationMaster = stationMaster;
	}

}
