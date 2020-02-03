package de.hsrm.mi.swtp.exchangeplatform.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFormat {
	
	public static String boxed(final String text) {
		String vert = "─".repeat(text.length() + 4);
		return String.format("\n┌%s┐\n│  %s  │\n└%s┘", vert, text, vert);
	}
	
	public static void infoBoxed(final String text) {
		log.info(LoggingFormat.boxed(text));
	}
	
	public static void warnBoxed(final String text) {
		log.warn(LoggingFormat.boxed(text));
	}
	
	public static void errorBoxed(final String text) {
		log.error(LoggingFormat.boxed(text));
	}
	
}
