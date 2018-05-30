package com.gate.barcode.check.gatepass.controller;

import java.io.File;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gate.barcode.check.gatepass.dto.DeleteTicketsRequest;
import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.request.AssignTicketsRequest;
import com.gate.barcode.check.gatepass.request.BarcodeCreationRequest;
import com.gate.barcode.check.gatepass.response.TicketResponse;
import com.gate.barcode.check.gatepass.service.CommonService;
import com.gate.barcode.check.gatepass.service.TicketService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	@Autowired
	private CommonService commonService;
	private static String UPLOADED_FOLDER = "barcode";
	
	@ApiOperation(value = "Generate desired no. of barcode.", notes = "Generate desired no. of barcode")
	@RequestMapping(value = "/genBarcode", method = RequestMethod.POST)
	public ResponseEntity<Object> createsdBarcode(@RequestHeader Long userId,
			@RequestBody BarcodeCreationRequest barcodeCreationRequest) throws Exception {
		commonService.checkUserType(userId);
		File directory = new File(UPLOADED_FOLDER);
		File filePath = null;
		if (!directory.exists()) {
			directory.mkdir();
		}
		String barCodePath = directory.getPath().concat(File.separator);
//		Path path = Paths
//				.get(UPLOADED_FOLDER + "//");
		long num = barcodeCreationRequest.getNoOfBarcode();
		int firstIndex = -1;
		int lastIndex = -1;
		for (int i = 1; i <= num; i++) {
			UUID uuid = UUID.randomUUID();
			long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();

			String uniqueID = Long.toString(l, Character.MAX_RADIX);

			Ticket ticket = ticketService.generateBarcode(userId, uniqueID, barCodePath,
					barcodeCreationRequest);
			if(i==1)
				firstIndex=Integer.valueOf(""+ticket.getId());
			if(i==num)
				lastIndex = Integer.valueOf(""+ticket.getId());
		}

		return new ResponseEntity<Object>("Barcode created successfully fron ID: "+firstIndex+" to ID: "+lastIndex, HttpStatus.OK);
	}

	@ApiOperation(value = "Get all barcode", notes = "Get all barcodes")
	@RequestMapping(value = "/getAllBarcode", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllBarcode(@RequestHeader Long userId,
			@RequestParam(required = false,value = "search") String search,
			@RequestParam(required = false,value="searchValue", defaultValue = "0") int searchValue,
			@RequestParam(value = "sort", required = false) Direction sort,
			@RequestParam(value = "page", required = false,defaultValue="0") int page,
			@RequestParam(value = "size", required = false,defaultValue="50") int size) {
		Map<Object,Object> ticketResponseList = ticketService.getAllBarcode(userId,search,searchValue,sort,page,size);
		return new ResponseEntity<Object>(ticketResponseList, HttpStatus.OK);
	}

	@ApiOperation(value = "Get a barcode", notes = "Get a barcode")
	@RequestMapping(value = "/getBarcode/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getBarcode(@RequestHeader Long userId,@PathVariable Long id) {
		TicketResponse ticketResponse = ticketService.getBarcode(userId,id);
		return new ResponseEntity<Object>(ticketResponse, HttpStatus.OK);

	}

	@ApiOperation(value = "Check barcode", notes = "check barcode")
	@RequestMapping(value = "/checkBarcode/{uniqueId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Object> checkBarcode(@PathVariable String uniqueId,
			@PathVariable Long userId,@RequestParam(required = false) Long stationId,@RequestParam(required = false) Long gateId,@RequestParam(required = false)String stationName,@RequestParam(required = false)String gateName) {
		String responseValue = ticketService.checkBarcode(uniqueId, userId,stationId,gateId,stationName,gateName);
		System.out.println("Station Id---->>>"+stationId);
		System.out.println("Gate Id------>>>>"+gateId);
		Map<Object,Object> res = new HashMap<Object,Object>();
		res.put("response", responseValue);
		return new ResponseEntity<Object>(res,HttpStatus.OK);

	}

	@RequestMapping(value = "/assignTickets", method = RequestMethod.PUT)
	public ResponseEntity<Object> assignTickets(@RequestHeader Long userId,
			@RequestBody AssignTicketsRequest request) {
		String totalAssigned = ticketService.assignTickets(userId, request);
		return new ResponseEntity<Object>(
				"Successfully Assigned, Total "+totalAssigned+" tickets assigned.", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deleteTickets", method = RequestMethod.PUT)
	public ResponseEntity<Object> deleteTickets(@RequestHeader Long userId,
			@RequestBody DeleteTicketsRequest request) {
		String totalDeleted = ticketService.deleteTickets(userId, request);
		return new ResponseEntity<Object>("Total "+totalDeleted+" tickets deleted.", HttpStatus.OK);
	}
	
	@RequestMapping(value="/getRecords", method=RequestMethod.GET)
	public ResponseEntity<Object> getRecords(@RequestHeader Long userId,
			@RequestParam(required = false,value = "search") String search,
			@RequestParam(required = false,value="searchValue", defaultValue = "0") int searchValue,
			@RequestParam(value = "sort", required = false) Direction sort,
			@RequestParam(value = "page", required = false,defaultValue="0") int page,
			@RequestParam(value = "size", required = false,defaultValue="50") int size) throws ParseException{
		Map<Object, Object> response = ticketService.getReports(userId,search,searchValue,sort,page,size);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteTicket(@RequestHeader Long userId,@PathVariable Long id){
		commonService.checkUserType(userId);
		ticketService.doDelete(id);
		return new ResponseEntity<Object>("Successfully Deleted", HttpStatus.OK);
	}
	@RequestMapping(value="/lastIndex",method=RequestMethod.GET)
	public ResponseEntity<Object> getLastIndex(@RequestHeader Long userId){
		commonService.checkUserType(userId);
		Long index = ticketService.getLastIndex();
		return new ResponseEntity<Object>(index,HttpStatus.OK);
	}
}
