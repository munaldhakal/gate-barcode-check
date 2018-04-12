package com.gate.barcode.check.gatepass.controller;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.request.AssignTicketsRequest;
import com.gate.barcode.check.gatepass.request.BarcodeCreationRequest;
import com.gate.barcode.check.gatepass.response.TicketResponse;
import com.gate.barcode.check.gatepass.service.TicketService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@ApiOperation(value = "Generate desired no. of barcode.", notes = "Generate desired no. of barcode")
	@RequestMapping(value = "/genBarcode", method = RequestMethod.POST)
	public ResponseEntity<Object> createsdBarcode(
			@RequestBody BarcodeCreationRequest barcodeCreationRequest) throws Exception {
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

	@ApiOperation(value = "Get all barcode", notes = "Get all barcodes")
	@RequestMapping(value = "/getAllBarcode", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllBarcode() {
		List<TicketResponse> ticketResponseList = ticketService.getAllBarcode();
		return new ResponseEntity<Object>(ticketResponseList, HttpStatus.OK);
	}

	@ApiOperation(value = "Get a barcode", notes = "Get a barcode")
	@RequestMapping(value = "getBarcode/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getBarcode(@PathVariable Long id) {
		TicketResponse ticketResponse = ticketService.getBarcode(id);
		return new ResponseEntity<Object>(ticketResponse, HttpStatus.OK);

	}

	@ApiOperation(value = "Check barcode", notes = "check barcode")
	@RequestMapping(value = "/checkBarcode/{uniqueId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Object> checkBarcode(@PathVariable String uniqueId,
			@PathVariable Long userId) {
		ticketService.checkBarcode(uniqueId, userId);
		return new ResponseEntity<Object>(HttpStatus.OK);

	}

	@RequestMapping(value = "/assignTickets", method = RequestMethod.PUT)
	public ResponseEntity<Object> assignTickets(@RequestHeader Long userId,
			@RequestBody AssignTicketsRequest request) {
		ticketService.assignTickets(userId, request);
		return new ResponseEntity<Object>(
				"Successfully Assigned to: " + request.getStationMaster(), HttpStatus.OK);
	}
}
