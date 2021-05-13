package br.com.api.exceptions.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String menssagem) {
		super(menssagem);
	}

	public FileNotFoundException(String menssagem, Throwable cause) {
		super(menssagem, cause);
	}

}
