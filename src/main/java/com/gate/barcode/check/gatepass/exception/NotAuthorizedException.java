package com.gate.barcode.check.gatepass.exception;

import org.hibernate.service.spi.ServiceException;

@SuppressWarnings("serial")
public class NotAuthorizedException extends ServiceException{
	
	public NotAuthorizedException(String string) {
		super(string);
	}

}
