package com.gate.barcode.check.gatepass.request;

public class StationCreationRequest {
	
	private String stationName;
	private Long  stationMaster;
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
