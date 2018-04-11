package com.gate.barcode.check.gatepass.utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * <<Description Here>>
 * @author Yubaraj
 * @version
 * @since , Dec 22, 2017
 */
public class Base64Util {
	/**
	 * 
	 * Encodes {@link File} to Base64.
	 * 
	 * @param file
	 * @return encoded string
	 * @throws IOException
	 * @author Yuba Raj Kalathoki
	 * @version 0.0.1
	 * @since 0.0.1 ,Modified In: @version,By @author
	 */

	public static String encodeFileToBase64Binary(File file) throws IOException {
		if (file == null)
			return "";
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

	private static void extracted(File file) throws IOException {
		throw new IOException("Could not completely read file " + file.getName());
	}
}
