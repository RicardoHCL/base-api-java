package br.com.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CustomException(String menssagem) {
		super(menssagem);
	}

}