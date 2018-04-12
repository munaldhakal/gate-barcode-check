package com.gate.barcode.check.gatepass.exception;

import org.hibernate.service.spi.ServiceException;

@SuppressWarnings("serial")
public class NotFoundException extends ServiceException {
	public NotFoundException(String message) {
		super(message);
	}

}
