package br.com.api.exceptions.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FileStorageException(String menssagem) {
		super(menssagem);
	}
	
	public FileStorageException(String menssagem, Throwable cause) {
		super(menssagem, cause);
	}

}