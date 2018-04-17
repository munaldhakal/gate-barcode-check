package com.gate.barcode.check.gatepass.response;

public class StationResponse {
	
	private Long id;
	private String stationName;
	private Long stationMaster;
	private String stationMasterName;
	
	public String getStationMasterName() {
		return stationMasterName;
	}
	public void setStationMasterName(String stationMasterName) {
		this.stationMasterName = stationMasterName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
