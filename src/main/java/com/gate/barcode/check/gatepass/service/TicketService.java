package com.gate.barcode.check.gatepass.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.barcode.check.gatepass.model.Ticket;
import com.gate.barcode.check.gatepass.repository.TicketRepository;
import com.gate.barcode.check.gatepass.request.BarcodeCreationRequest;
import com.gate.barcode.check.gatepass.response.TicketResponse;
import com.gate.barcode.check.gatepass.utilities.TicketStatus;
import org.apache.tomcat.util.codec.binary.Base64;

@Service
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	public Ticket generateBarcode(String uniqueID, String barCodePath, BarcodeCreationRequest barcodeCreationRequest) {

		// System.out.println("I am here");
		// long num = barcodeCreationRequest.getNoOfBarcode();

		try {
			Code128Bean bean = new Code128Bean();
			final int dpi = 160;

			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

			bean.doQuietZone(false);
			// File file=null;
			File outputFile = new File(barCodePath + uniqueID + ".JPG");
			//File outFile = new File("C:\\Users\\Lothbroke\\Desktop\\Softech\\barcode\\r1dcpntx4px3.JPG");

			System.out.println("path:" + outputFile);

			Ticket ticket = new Ticket();

			FileOutputStream out = new FileOutputStream(outputFile);

			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			bean.generateBarcode(canvas, uniqueID);
			String path = outputFile.getAbsolutePath();
			ticket.setBarcode(path);

			ticket.setTicketStatus(TicketStatus.ACTIVE);
			ticket.setCreatedBy(barcodeCreationRequest.getCreatedBy());
			ticket.setCreatedDate(new Date());
			ticket.setUniqueId(uniqueID);
			ticketRepository.save(ticket);

			canvas.finish();
		//	String encodstring = encodeFileToBase64Binary(outFile);
			//System.out.println("HHHHHHHHH:" + encodstring);

			System.out.println("Bar Code is generated successfully…");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static String encodeFileToBase64Binary(File file) throws IOException {
		// String encodedfile = null;

		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		// encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");

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

	public List<TicketResponse> getBarcode() {

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

}
