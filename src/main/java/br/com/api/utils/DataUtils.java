package br.com.api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtils {
	
	
	public static String getStringComDataHoraAtual() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
		LocalDateTime dateTime = LocalDateTime.now();
		
		return dateTime.format(formatter);
	}

}
