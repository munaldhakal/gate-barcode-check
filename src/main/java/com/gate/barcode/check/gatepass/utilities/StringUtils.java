package com.gate.barcode.check.gatepass.utilities;


import java.util.List;

/**
 * String utility class
 * 
 * @author Munal Dhakal
 * @version 1.0.0
 * @since 1.0.0, Mar 14, 2017
 */
public class StringUtils {
	public static boolean contains(List<String> stringList, String value) {
		return stringList.contains(value);
	}

	public static boolean contains(String value) {
		return value.matches(SortByUser.values().toString());
	}

	public static String capitalizeFirstLetter(String original) {
		if (original == null || original.length() == 0) {
			return original;
		}
		String lowerCase = original.toLowerCase();
		return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
	}

}
