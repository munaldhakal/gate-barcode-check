package com.gate.barcode.check.gatepass.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.service.spi.ServiceException;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.dto.DeleteTicketsRequest;
import com.gate.barcode.check.gatepass.exception.NotFoundException;
import com.gate.barcode.check.gatepass.model.Gate;
import com.gate.barcode.check.gatepass.model.Station;
import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.model.User;
import com.gate.barcode.check.gatepass.repository.GateRepository;
import com.gate.barcode.check.gatepass.repository.StationRepository;
import com.gate.barcode.check.gatepass.repository.TicketRepository;
import com.gate.barcode.check.gatepass.repository.UserRepository;
import com.gate.barcode.check.gatepass.request.AssignTicketsRequest;
import com.gate.barcode.check.gatepass.request.BarcodeCreationRequest;
import com.gate.barcode.check.gatepass.response.RecordResponse;
import com.gate.barcode.check.gatepass.response.TicketResponse;
import com.gate.barcode.check.gatepass.utilities.TicketStatus;
import com.gate.barcode.check.gatepass.utilities.UserType;

@Service
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommonService commonService;
	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private GateRepository gateRepository;

	@Transactional
	public Ticket generateBarcode(Long userId, String uniqueID, String barCodePath,
			BarcodeCreationRequest barcodeCreationRequest) {
		commonService.checkUserType(userId);
		Ticket ticket = new Ticket();
		try {
			Code128Bean bean = new Code128Bean();
			final int dpi = 160;
			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

			bean.doQuietZone(false);
			File outputFile = new File(barCodePath + uniqueID + ".png");
			System.out.println("path:" + outputFile);

			FileOutputStream out = new FileOutputStream(outputFile);

			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			bean.generateBarcode(canvas, uniqueID);
			String path = outputFile.getAbsolutePath();
			ticket.setBarcode(path);
			ticket.setTicketStatus(TicketStatus.UNVERIFIED);
			ticket.setCreatedBy(userId);
			ticket.setCreaterName(userRepository.getOne(userId).getName());
			ticket.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			ticket.setPrice(barcodeCreationRequest.getPrice());
			ticket.setUniqueId(uniqueID);
			ticket.setStationMasterId(barcodeCreationRequest.getStationMaster());
			ticket.setStationMasterName(userRepository.getOne(ticket.getStationMasterId()).getName());
			ticket = ticketRepository.save(ticket);

			canvas.finish();

			System.out.println("Bar Code is generated successfully…");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ticket;
	}

	private String encodeFileToBase64Binary(File file) throws IOException {

		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	private byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// file is too large. :P
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			extracted(file);
		}
		is.close();
		return bytes;
	}

	private void extracted(File file) throws IOException {
		throw new IOException("Could not completely read file " + file.getName());
	}

	

	private TicketResponse getTicket(Ticket u) {
		TicketResponse ticketResponse = new TicketResponse();
		ticketResponse.setId(u.getId());
		String file = u.getBarcode();
		File outputFile = new File(file);
		ticketResponse.setPrice(u.getPrice());
		try {
			String encodstring = encodeFileToBase64Binary(outputFile);
			ticketResponse.setBarcode(encodstring);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ticketResponse.setUniqueId(u.getUniqueId());
		return ticketResponse;
	}

	public TicketResponse getBarcode(Long userId, Long id) {
		Optional<Ticket> ticket = null;
		if (commonService.getUserType(userId).equals(UserType.ADMIN))
			ticket = ticketRepository.findById(id);
		else if (commonService.getUserType(userId).equals(UserType.TICKETCHECKER)) {
			throw new ServiceException("Sorry You Are not Authorized");
		} else {
			ticket = ticketRepository.findByStationMasterId(userId);
		}
		if (!ticket.isPresent()) {
			throw new NotFoundException("Barcode with id=" + id + " not found. Try with different ID.");
		}
		return getTicket(ticket.get());
	}

	public String checkBarcode(String uniqueId, Long userId, Long stationId, Long gateId, String stationName,
			String gateName) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new ServiceException("User with id:" + userId + " not found.");
		}
		Ticket ticket = ticketRepository.findByUniqueIdAndTicketStatusNot(uniqueId, TicketStatus.BLOCKED);
		if (ticket == null) {
			System.out.println("Inside if");
			throw new ServiceException("Sorry ticket is no more valid");

		}
		if (ticket.getTicketStatus().equals(TicketStatus.UNVERIFIED)
				&& (commonService.getUserType(userId).equals(UserType.ADMIN)
						|| commonService.getUserType(userId).equals(UserType.STATIONMASTER))) {
			ticket.setTicketStatus(TicketStatus.VERIFIED);
			ticket.setIssuedBy(userId);
			ticket.setIssuerName(userRepository.getOne(userId).getName());
			ticket.setIssuedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			if (stationId != null) {
				Optional<Station> st = stationRepository.findById(stationId);
				if (!st.isPresent())
					throw new ServiceException("Sorry no station selected");
				ticket.setStationId(stationId);
				ticket.setStationName(st.get().getStationName());
			} else if (stationName != null) {
				Station st = stationRepository.findByStationName(stationName);
				if (st == null)
					throw new ServiceException("Sorry No station selected");
				ticket.setStationId(st.getId());
				ticket.setStationName(stationName);
			} else {
				throw new ServiceException("Please Select a station");
			}
			ticketRepository.save(ticket);
			return "TICKET VERIFIED SUCCESSFULLY";
		}
		if (ticket.getTicketStatus().equals(TicketStatus.VERIFIED)) {
			ticket.setTicketStatus(TicketStatus.BLOCKED);
			ticket.setCheckedBy(userId);
			ticket.setCheckerName(userRepository.getOne(userId).getName());
			if (gateId != null) {
				Optional<Gate> gt = gateRepository.findById(gateId);
				if (!gt.isPresent())
					throw new ServiceException("Sorry no gate selected");
				ticket.setGateId(gateId);
				ticket.setGateName(gt.get().getGateName());
			} else if (gateName != null) {
				Gate gt = gateRepository.findByGateName(gateName);
				if (gt == null)
					throw new ServiceException("Sorry No gate found of name :" + gateName);
				ticket.setGateId(gt.getId());
				ticket.setGateName(gateName);
			} else {
				throw new ServiceException("Please select a gate");
			}
			ticket.setCheckedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			ticketRepository.save(ticket);
			return "TICKET BLOCKED SUCCESSFULLY";
		}
		throw new ServiceException("ERROR PLEASE TRY AGAIN");
	}

	/**
	 * <<This method assigns already assigned tickets to the stationmaster>>
	 * 
	 * @param userId
	 * @param request
	 * @author Munal
	 * @since 12/04/2018, Modified In: @version, By @author
	 */
	@Transactional
	public String assignTickets(Long userId, AssignTicketsRequest request) {
		if(request.getIdFrom()>request.getIdTo())
			throw new ServiceException("Wrong Formaat: Please Correct as E.G=> From :1 To:100");
		if(request.getIdTo()>getLastIndex())
			throw new ServiceException("Sorry. You cannot assign Ids which are not created. Please Check 'ID TO'");
		commonService.checkUserType(userId);
		Double count = Double.valueOf("0");
		for (Long i = request.getIdFrom(); i <= request.getIdTo(); i++) {
			Ticket ticket = ticketRepository.findByIdAndTicketStatus(i, TicketStatus.UNVERIFIED);
			if (ticket == null)
				continue;
			ticket.setModifiedBy(userId);
			ticket.setModifierName(userRepository.getOne(userId).getName());
			ticket.setModifiedDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			if (request.getPrice() != null)
				ticket.setPrice(request.getPrice());
			ticket.setStationMasterId(request.getStationMaster());
			ticket.setStationMasterName(userRepository.getOne(ticket.getStationMasterId()).getName());
			count++;
			ticketRepository.save(ticket);
		}
		return new BigDecimal(String.valueOf(count)).toPlainString();
	}
	
	@Transactional
	public String deleteTickets(Long userId, DeleteTicketsRequest request) {
		if(request.getIdFrom()>request.getIdTo())
			throw new ServiceException("Wrong Formaat: Please Correct as E.G=> From :1 To:100");
		if(request.getIdTo()>getLastIndex())
			throw new ServiceException("Sorry. You cannot assign Ids which are not created. Please Check 'ID TO'");
		commonService.checkUserType(userId);
		Double count = Double.valueOf("0");
		for (Long i = request.getIdFrom(); i <= request.getIdTo(); i++) {
			Optional<Ticket> ticket = ticketRepository.findById(i);
			if (!ticket.isPresent()) 
				continue;
			count++;
			System.out.println("Deleted Id :--->>>>>>>>>>>>" + ticket.get().getId());
			ticketRepository.delete(ticket.get());
		}
		return new BigDecimal(String.valueOf(count)).toPlainString();
	}
	@SuppressWarnings("deprecation")
	@Transactional
	public Map<Object,Object> getAllBarcode(Long userId, String search, int searchValue, Direction sort, int page,
			int size) {
		if (sort == null) {
			sort = Direction.DESC;
		}
		Page<Ticket> tickets = null;
		if (commonService.getUserType(userId).equals(UserType.ADMIN)) {
			if (search == null || search.isEmpty() || search == "")
				tickets = ticketRepository.findAllByTicketStatus(TicketStatus.UNVERIFIED,new PageRequest(page, size, Direction.DESC, "createdDate"));
			else {
				if(searchValue==1)
					tickets = ticketRepository.findAllByIdAndTicketStatus(Long.valueOf(search),TicketStatus.UNVERIFIED,new PageRequest(page, size, sort, getTicketSearchIndex(searchValue)));
				else if(searchValue==2)
					tickets = ticketRepository.findAllByPriceAndTicketStatus(search,TicketStatus.UNVERIFIED,new PageRequest(page, size, sort, getTicketSearchIndex(searchValue)));
				else if(searchValue==3)
					tickets = ticketRepository.findAllByUniqueIdAndTicketStatus(search,TicketStatus.UNVERIFIED,new PageRequest(page, size, sort, getTicketSearchIndex(searchValue)));
				else
					throw new ServiceException("Please select appropriate searchValue");
			}
		} else if (commonService.getUserType(userId).equals(UserType.TICKETCHECKER)) {
			throw new ServiceException("Sorry You Are not Authorized");
		} else {
			if (search == null || search.isEmpty() || search == "")
				tickets = ticketRepository.findAllByStationMasterIdAndTicketStatus(userId, TicketStatus.UNVERIFIED,new PageRequest(page, size, Direction.DESC, "createdDate"));
			else {
				if(searchValue==1)
					tickets = ticketRepository.findAllByIdAndStationMasterIdAndTicketStatus(Long.valueOf(search),userId, TicketStatus.UNVERIFIED,new PageRequest(page, size, sort, getTicketSearchIndex(searchValue)));
				else if(searchValue==2)
					tickets = ticketRepository.findAllByPriceAndStationMasterIdAndTicketStatus(search,userId, TicketStatus.UNVERIFIED,new PageRequest(page, size, sort, getTicketSearchIndex(searchValue)));
				else if(searchValue==3)
					tickets = ticketRepository.findAllByUniqueIdAndStationMasterIdAndTicketStatus(search,userId, TicketStatus.UNVERIFIED,new PageRequest(page, size, sort, getTicketSearchIndex(searchValue)));
				else
					throw new ServiceException("Please select appropriate searchValue");
			}
		}
		if (tickets == null)
			throw new NotFoundException("No Tickets Found.. Please Create One");
		List<TicketResponse> ticketResponseList = new ArrayList<TicketResponse>();
		for (Ticket u : tickets) {
			ticketResponseList.add(getTicket(u));
		}
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		responseMap.put("response", ticketResponseList);
		responseMap.put("noOfItems", tickets.getNumberOfElements());
		responseMap.put("totalNoOfItems", tickets.getTotalElements());
		responseMap.put("noOfPages", tickets.getTotalPages());
		responseMap.put("pageNumber", tickets.getNumber());
		return responseMap;

	}
	
	/**
	 * <<This method returns the records for the users>>
	 * 
	 * @param userId
	 * @return List<RecordResponse>
	 * @author Munal
	 * @param size
	 * @param page
	 * @param sort
	 * @param searchValue
	 * @param search
	 * @throws ParseException 
	 * @since 16/04/2018, Modified In: @version, By @author
	 */
	@SuppressWarnings("deprecation")
	public Map<Object, Object> getReports(Long userId, String search, int searchValue, Direction sort, int page,
			int size) throws ParseException {
		System.out.println("Date----->>>>>>>>>>>>>>>>>>>>"+search);
		UserType type = commonService.getUserType(userId);
		if (sort == null) {
			sort = Direction.DESC;
		}
		Page<Ticket> ticket = null;
		if (type.equals(UserType.TICKETCHECKER)) {
			if (search == null || search.isEmpty() || search == "")
				ticket = ticketRepository.findByCheckedBy(userId,
						new PageRequest(page, size, Direction.DESC, "issuedDate"));
			else {
				if(searchValue==1)
					ticket = ticketRepository.findByIdAndCheckedBy(Long.valueOf(search),userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==2)
					ticket = ticketRepository.findByPriceAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==3)
					ticket = ticketRepository.findByTicketStatusAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==4)
					ticket = ticketRepository.findByStationNameAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==5)
					ticket = ticketRepository.findByStationMasterNameAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==6)
					ticket = ticketRepository.findByGateNameAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==7)
					ticket = ticketRepository.findByCreaterNameAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==8) {
					ticket = ticketRepository.findByCreatedDateAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				}
				else if(searchValue==9)
					ticket = ticketRepository.findByIssuerNameAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==10)
					ticket = ticketRepository.findByIssuedDateAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==11)
					ticket = ticketRepository.findByCheckerNameAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==12)
					ticket = ticketRepository.findByCheckedDateAndCheckedBy(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else 
					throw new ServiceException("Please Selsect appropriate selectValue");
			}
		} else if (type.equals(UserType.STATIONMASTER)) {
			if (search == null || search.isEmpty() || search == "")
				ticket = ticketRepository.findByStationMasterId(userId,
						new PageRequest(page, size, Direction.DESC, "issuedDate"));
			else {
				if(searchValue==1)
					ticket = ticketRepository.findByIdAndStationMasterId(Long.valueOf(search),userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==2)
					ticket = ticketRepository.findByPriceAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==3)
					ticket = ticketRepository.findByTicketStatusAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==4)
					ticket = ticketRepository.findByStationNameAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==5)
					ticket = ticketRepository.findByStationMasterNameAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==6)
					ticket = ticketRepository.findByGateNameAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==7)
					ticket = ticketRepository.findByCreaterNameAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==8)
					ticket = ticketRepository.findByCreatedDateAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==9)
					ticket = ticketRepository.findByIssuerNameAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==10)
					ticket = ticketRepository.findByIssuedDateAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==11)
					ticket = ticketRepository.findByCheckerNameAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==12)
					ticket = ticketRepository.findByCheckedDateAndStationMasterId(search,userId,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else 
					throw new ServiceException("Please Selsect appropriate selectValue");
			}
		} else {
			if (search == null || search.isEmpty() || search == "")
				ticket = ticketRepository.findAll(new PageRequest(page, size, Direction.DESC, "issuedDate"));
			else {
				if(searchValue==1)
					ticket = ticketRepository.findById(Long.valueOf(search),
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==2)
					ticket = ticketRepository.findByPrice(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==3)
					ticket = ticketRepository.findByTicketStatus(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==4)
					ticket = ticketRepository.findByStationName(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==5)
					ticket = ticketRepository.findByStationMasterName(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==6)
					ticket = ticketRepository.findByGateName(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==7)
					ticket = ticketRepository.findByCreaterName(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==8)
					ticket = ticketRepository.findByCreatedDate(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==9)
					ticket = ticketRepository.findByIssuerName(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==10)
					ticket = ticketRepository.findByIssuedDate(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==11)
					ticket = ticketRepository.findByCheckerName(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else if(searchValue==12)
					ticket = ticketRepository.findByCheckedDate(search,
							new PageRequest(page, size, sort, getSearchIndexParameter(searchValue)));
				else 
					throw new ServiceException("Please Selsect appropriate selectValue");
			}
		}
		if (ticket == null)
			throw new ServiceException("No tickets Found");

		List<RecordResponse> response = new ArrayList<>();
		for (Ticket t : ticket) {
			RecordResponse r = new RecordResponse();
			r.setTicketId(t.getId());
			if (t.getCheckedBy() != null) {
				r.setCheckedBy(t.getCheckedBy());
				r.setCheckedByUserName(t.getCheckerName());
			}

			if (t.getCheckedDate() != null)
				r.setCheckedDate(t.getCheckedDate());
			if (t.getCreatedBy() != null) {
				r.setCreatedBy(t.getCreatedBy());
				r.setCreatedByUserName(t.getCreaterName());

			}
			if (t.getCreatedDate() != null)
				r.setCreatedDate(t.getCreatedDate());
			if (t.getGateId() != null) {
				r.setGateId(t.getGateId());
				r.setGateName(t.getGateName());
			}
			if (t.getIssuedBy() != null) {
				r.setIssuedBy(t.getIssuedBy());
				r.setIssuedByUserName(t.getIssuerName());
			}
			if (t.getIssuedDate() != null)
				r.setIssuedDate(t.getIssuedDate());
			if (t.getModifiedBy() != null) {
				r.setModifiedBy(t.getModifiedBy());
				r.setModifiedByUserName(t.getModifierName());
			}
			if (t.getModifiedDate() != null)
				r.setModifiedDate(t.getModifiedDate());
			if (t.getPrice() != null)
				r.setPrice(t.getPrice());
			if (t.getStationId() != null) {
				r.setStationId(t.getStationId());
				r.setStationName(t.getStationName());
			}

			if (t.getStationMasterId() != null) {
				r.setStationMaster(t.getStationMasterId());
				r.setStationMasterName(t.getStationMasterName());
			}
			if (t.getTicketStatus() != null)
				r.setTicketStatus(t.getTicketStatus());
			if (t.getUniqueId() != null)
				r.setUniqueId(t.getUniqueId());
			response.add(r);
		}
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		responseMap.put("response", response);
		responseMap.put("noOfItems", ticket.getNumberOfElements());
		responseMap.put("totalNoOfItems", ticket.getTotalElements());
		responseMap.put("noOfPages", ticket.getTotalPages());
		responseMap.put("pageNumber", ticket.getNumber());
		return responseMap;
	}

	/**
	 * <<Deletes a ticket>>
	 * 
	 * @param id
	 * @author Munal
	 * @since 18/04/2018, Modified In: @version, By @author
	 */
	@Transactional
	public void doDelete(Long id) {
		Optional<Ticket> ticket = ticketRepository.findById(id);
		if (!ticket.isPresent()) {
			throw new ServiceException("No ticket Found Of Id: " + id);
		}
		System.out.println("Deleted Id :--->>>>>>>>>>>>" + ticket.get().getId());
		ticketRepository.delete(ticket.get());
	}

	public Long getLastIndex() {
		return ticketRepository.getLastIndex();
	}

	private String getSearchIndexParameter(int value) {
		switch (value) {
		case 1:
			return "id";
		case 2:
			return "price";
		case 3:
			return "ticketStatus";
		case 4:
			return "stationName";
		case 5:
			return "stationMasterName";
		case 6:
			return "gateName";
		case 7:
			return "createrName";
		case 8:
			return "createdDate";
		case 9:
			return "issuerName";
		case 10:
			return "issuedDate";
		case 11:
			return "checkerName";
		case 12:
			return "checkedDate";
		default:
			throw new ServiceException("Please select appropriate searchValue");
		}
	}
	private String getTicketSearchIndex(int value) {
		switch (value) {
		case 1:
			return "id";
		case 2:
			return "price";
		case 3:
			return "uniqueId";
		default:
			throw new ServiceException("Please select appropriate searchValue");
		}
	}
}
