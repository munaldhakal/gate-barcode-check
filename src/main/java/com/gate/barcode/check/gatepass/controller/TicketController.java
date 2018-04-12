package com.gate.barcode.check.gatepass.controller;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.request.BarcodeCreationRequest;
import com.gate.barcode.check.gatepass.response.TicketResponse;
import com.gate.barcode.check.gatepass.service.TicketService;

import ch.qos.logback.core.status.Status;

@RestController
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@RequestMapping(value = "GenBarcode", method = RequestMethod.POST)
	public ResponseEntity<Object> createsdBarcode(@RequestBody BarcodeCreationRequest barcodeCreationRequest)
			throws Exception {
		String barCodePath = "C:\\Users\\Lothbroke\\Desktop\\Softech\\barcode\\";
		long num = barcodeCreationRequest.getNoOfBarcode();
		for (int i = 1; i <= num; i++) {
			UUID uuid = UUID.randomUUID();
			long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();

			String uniqueID = Long.toString(l, Character.MAX_RADIX);

			ticketService.generateBarcode(uniqueID, barCodePath, barcodeCreationRequest);
		}

		return new ResponseEntity<Object>("Barcode created successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value="GetBarcode",method=RequestMethod.GET)
	public ResponseEntity<Object> getAllBarcode(){
		List<TicketResponse> ticketResponseList=ticketService.getBarcode();
		return new ResponseEntity<Object>(ticketResponseList,HttpStatus.OK);
	}

}
