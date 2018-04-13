package com.gate.barcode.check.gatepass.request;

import javax.validation.constraints.NotNull;

public class StationEditRequest {
	@NotNull(message="Id to edit cannot be null")
	private long id;
	private String stationName;
	private Long stationMaster;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
