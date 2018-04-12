package com.gate.barcode.check.gatepass.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class BarcodeCreationRequest implements Serializable {
	@NotNull(message = "Total Number of barcode should not be null")
	private Long noOfBarcode;
	@NotNull(message = "Price cannot be null")
	private String price;
	@NotNull(message = "station Master Id cannot be null")
	private Long stationMaster;
	

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Long getNoOfBarcode() {
		return noOfBarcode;
	}

	public void setNoOfBarcode(Long noOfBarcode) {
		this.noOfBarcode = noOfBarcode;
	}

	public Long getStationMaster() {
		return stationMaster;
	}

	public void setStationMaster(Long stationMaster) {
		this.stationMaster = stationMaster;
	}
	
}
