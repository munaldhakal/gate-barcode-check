package com.gate.barcode.check.gatepass.exception;

import org.hibernate.service.spi.ServiceException;

public class AlreadyExistException extends ServiceException{
	public AlreadyExistException(String string) {
		super(string);
	}

}
