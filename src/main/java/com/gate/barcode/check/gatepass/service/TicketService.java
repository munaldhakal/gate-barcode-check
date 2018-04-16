package com.gate.barcode.check.gatepass.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.service.spi.ServiceException;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.exception.NotFoundException;
import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.model.User;
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

			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png",
					dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

			bean.generateBarcode(canvas, uniqueID);
			String path = outputFile.getAbsolutePath();
			ticket.setBarcode(path);

			ticket.setTicketStatus(TicketStatus.UNVERIFIED);
			ticket.setCreatedBy(userId);
			ticket.setCreatedDate(new Date());
			ticket.setPrice(barcodeCreationRequest.getPrice());
			ticket.setUniqueId(uniqueID);
			ticket.setStationMaster(barcodeCreationRequest.getStationMaster());
			ticket = ticketRepository.save(ticket);

			canvas.finish();

			System.out.println("Bar Code is generated successfully…");
		}
		catch (Exception ex) {
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
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
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

	@Transactional
	public List<TicketResponse> getAllBarcode(Long userId) {
		List<Ticket> tickets = null;
		if (commonService.getUserType(userId).equals(UserType.ADMIN)) {
			tickets = ticketRepository.findAll();
		}
		else if (commonService.getUserType(userId).equals(UserType.TICKETCHECKOR)) {
			throw new ServiceException("Sorry You Are not Authorized");
		}
		else {
			tickets = ticketRepository.findAllByStationMaster(userId);
		}
		if (tickets == null)
			throw new NotFoundException("No Tickets Found.. Please Create One");
		List<TicketResponse> ticketResponseList = new ArrayList<TicketResponse>();
		tickets.stream().forEach(u -> {
			TicketResponse ticketResponse = new TicketResponse();
			ticketResponse.setId(u.getId());
			String file = u.getBarcode();
			File outputFile = new File(file);
			ticketResponse.setPrice(u.getPrice());
			try {
				String encodstring = encodeFileToBase64Binary(outputFile);
				ticketResponse.setBarcode(encodstring);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			ticketResponseList.add(ticketResponse);
		});
		return ticketResponseList;

	}

	public TicketResponse getBarcode(Long userId, Long id) {
		Optional<Ticket> ticket = null;
		if (commonService.getUserType(userId).equals(UserType.ADMIN))
			ticket = ticketRepository.findById(id);
		else if (commonService.getUserType(userId).equals(UserType.TICKETCHECKOR)) {
			throw new ServiceException("Sorry You Are not Authorized");
		}
		else {
			ticket = ticketRepository.findByStationMaster(userId);
		}
		if (!ticket.isPresent()) {
			throw new NotFoundException(
					"Barcode with id=" + id + " not found. Try with different ID.");
		}
		TicketResponse ticketResponse = new TicketResponse();
		ticketResponse.setId(ticket.get().getId());
		ticketResponse.setPrice(ticket.get().getPrice());
		String file = ticket.get().getBarcode();
		File outputFile = new File(file);
		try {
			String encodstring = encodeFileToBase64Binary(outputFile);
			ticketResponse.setBarcode(encodstring);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return ticketResponse;

	}

	public Boolean checkBarcode(String uniqueId, Long userId, Long stationId,
			Long gateId) {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new NotFoundException("User with id:" + userId + " not found.");
		}
		Ticket ticket = ticketRepository.findByUniqueIdAndTicketStatusNot(uniqueId,
				TicketStatus.BLOCKED);
		if (ticket == null) {
			throw new ServiceException("Sorry ticket is no more valid.");
		}
		if (ticket.getTicketStatus().equals(TicketStatus.UNVERIFIED) && (commonService
				.getUserType(userId).equals(UserType.ADMIN)
				|| commonService.getUserType(userId).equals(UserType.STATIONMASTER))) {
			ticket.setTicketStatus(TicketStatus.VERIFIED);
			ticket.setIssuedBy(userId);
			ticket.setIssuedDate(new Date());
			if (stationId != null)
				ticket.setStationId(stationId);
			else
				throw new ServiceException("Please Select a station");
			ticketRepository.save(ticket);
			return true;
		}
		if (ticket.getTicketStatus().equals(TicketStatus.VERIFIED)) {
			ticket.setTicketStatus(TicketStatus.BLOCKED);
			ticket.setCheckedBy(userId);
			if (gateId != null)
				ticket.setGateId(gateId);
			ticket.setCheckedDate(new Date());
			ticketRepository.save(ticket);
			return true;
		}
		return false;
	}

	/**
	 * <<This method assigns already assigned tickets to the stationmaster>>
	 * @param userId
	 * @param request
	 * @author Munal
	 * @since 12/04/2018, Modified In: @version, By @author
	 */
	@Transactional
	public void assignTickets(Long userId, AssignTicketsRequest request) {
		commonService.checkUserType(userId);
		for (Long i = request.getIdFrom(); i <= request.getIdTo(); i++) {
			Ticket ticket = ticketRepository.getOne(i);
			if (ticket == null)
				continue;
			ticket.setModifiedBy(userId);
			ticket.setModifiedDate(new Date());
			if (request.getPrice() != null)
				ticket.setPrice(request.getPrice());
			ticket.setStationMaster(request.getStationMaster());
			ticketRepository.save(ticket);
		}
	}

	/**
	 * <<This method returns the records for the users>>
	 * @param userId
	 * @return List<RecordResponse>
	 * @author Munal
	 * @since 16/04/2018, Modified In: @version, By @author
	 */
	public List<RecordResponse> getReports(Long userId) {
		UserType type = commonService.getUserType(userId);
		List<Ticket> ticket = null;
		if (type.equals(UserType.TICKETCHECKOR)) {
			ticket = ticketRepository.findByCheckedBy(userId);
		}
		else if (type.equals(UserType.STATIONMASTER)) {
			ticket = ticketRepository.findAllByStationMaster(userId);
		}
		else
			ticket = ticketRepository.findAll();
		if (ticket == null)
			throw new ServiceException("No tickets Found");
		List<RecordResponse> response = new ArrayList<>();
		for (Ticket t : ticket) {
			RecordResponse r = new RecordResponse();
			r.setTicketId(t.getId());
			if(t.getCheckedBy()!=null)
			r.setCheckedBy(t.getCheckedBy());
			if(t.getCheckedDate()!=null)
			r.setCheckedDate(new SimpleDateFormat("dd-MM-yyyy").format(t.getCheckedDate()));
			if(t.getCreatedBy()!=null)
			r.setCreatedBy(t.getCreatedBy());
			if(t.getCreatedDate()!=null)
			r.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy").format(t.getCreatedDate()));
			if(t.getGateId()!=null)
			r.setGateId(t.getGateId());
			if(t.getIssuedBy()!=null)
			r.setIssuedBy(t.getIssuedBy());
			if(t.getIssuedDate()!=null)
			r.setIssuedDate(new SimpleDateFormat("dd-MM-yyyy").format(t.getIssuedDate()));
			if(t.getModifiedBy()!=null)
			r.setModifiedBy(t.getModifiedBy());
			if(t.getModifiedDate()!=null)
			r.setModifiedDate(new SimpleDateFormat("dd-MM-yyyy").format(t.getModifiedDate()));
			if(t.getPrice()!=null)
			r.setPrice(t.getPrice());
			if(t.getStationId()!=null)
			r.setStationId(t.getStationId());
			if(t.getStationMaster()!=null)
			r.setStationMaster(t.getStationMaster());
			if(t.getTicketStatus()!=null)
			r.setTicketStatus(t.getTicketStatus());
			if(t.getUniqueId()!=null)
			r.setUniqueId(t.getUniqueId());
			response.add(r);
		}
		return response;
	}

}
