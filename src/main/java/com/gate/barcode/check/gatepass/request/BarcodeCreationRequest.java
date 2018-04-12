package com.gate.barcode.check.gatepass.request;

public class BarcodeCreationRequest {

	private Long createdBy;

	private Long noOfBarcode;

	public Long getNoOfBarcode() {
		return noOfBarcode;
	}

	public void setNoOfBarcode(Long noOfBarcode) {
		this.noOfBarcode = noOfBarcode;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

}
