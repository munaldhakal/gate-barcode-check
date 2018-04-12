package com.gate.barcode.check.gatepass.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.gate.barcode.check.gatepass.repository.TicketRepository;
import com.gate.barcode.check.gatepass.repository.UserRepository;
import com.gate.barcode.check.gatepass.request.BarcodeCreationRequest;
import com.gate.barcode.check.gatepass.response.TicketResponse;
import com.gate.barcode.check.gatepass.utilities.TicketStatus;


@Service
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public Ticket generateBarcode(String uniqueID, String barCodePath, BarcodeCreationRequest barcodeCreationRequest) {

		try {
			Code128Bean bean = new Code128Bean();
			final int dpi = 160;
			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

			bean.doQuietZone(false);
			File outputFile = new File(barCodePath + uniqueID + ".JPG");
			System.out.println("path:" + outputFile);

			FileOutputStream out = new FileOutputStream(outputFile);

			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			bean.generateBarcode(canvas, uniqueID);
			String path = outputFile.getAbsolutePath();
			Ticket ticket = new Ticket();
			ticket.setBarcode(path);

			ticket.setTicketStatus(TicketStatus.ACTIVE);
			ticket.setCreatedBy(barcodeCreationRequest.getCreatedBy());
			ticket.setCreatedDate(new Date());
			ticket.setPrice(barcodeCreationRequest.getPrice());
			ticket.setUniqueId(uniqueID);
			ticketRepository.save(ticket);

			canvas.finish();

			System.out.println("Bar Code is generated successfully…");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static String encodeFileToBase64Binary(File file) throws IOException {

		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	private static byte[] loadFile(File file) throws IOException {
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

	private static void extracted(File file) throws IOException {
		throw new IOException("Could not completely read file " + file.getName());
	}

	@Transactional
	public List<TicketResponse> getAllBarcode() {

		List<TicketResponse> ticketResponseList = new ArrayList<TicketResponse>();
		List<Ticket> tickets = ticketRepository.findAllByTicketStatusNot(TicketStatus.BLOCKED);
		tickets.stream().forEach(u -> {
			TicketResponse ticketResponse = new TicketResponse();
			ticketResponse.setId(u.getId());
			String file = u.getBarcode();
			File outputFile = new File(file);
			try {
				String encodstring = encodeFileToBase64Binary(outputFile);
				ticketResponse.setBarcode(encodstring);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ticketResponseList.add(ticketResponse);

		});
		return ticketResponseList;

	}

	public TicketResponse getBarcode(Long id) {
		Ticket ticket = ticketRepository.findByIdAndTicketStatusNot(id, TicketStatus.BLOCKED);
		if (ticket == null) {
			throw new NotFoundException("Barcode with id=" + id + " not found. Try with different ID.");
		}
		TicketResponse ticketResponse = new TicketResponse();
		ticketResponse.setId(ticket.getId());
		String file = ticket.getBarcode();
		File outputFile = new File(file);
		try {
			String encodstring = encodeFileToBase64Binary(outputFile);
			ticketResponse.setBarcode(encodstring);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticketResponse;

	}

	public void checkBarcode(String uniqueId, Long userId) {
		User user = userRepository.getOne(userId);
		if (user == null) {
			throw new NotFoundException("User with id:" + userId + " not found.");
		}
//		if (!(user.getUserType().equals(UserType.TICKETCHECKOR) || user.getUserType().equals(UserType.STATIONMASTER))) {
//			throw new NotAuthorizedException("Sorry you don't have enough privilege.");
//		}
		Ticket ticket = ticketRepository.findByUniqueIdAndTicketStatusNot(uniqueId, TicketStatus.BLOCKED);
		if (ticket == null) {
			throw new ServiceException("Sorry ticket is no more valid.");
		}
		ticket.setModifiedBy(userId);
		ticket.setTicketStatus(TicketStatus.BLOCKED);
		ticketRepository.save(ticket);
	}

}
