package br.com.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.JwtException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthenticationJwtException extends JwtException{

private static final long serialVersionUID = 1L;
	
	public AuthenticationJwtException(String menssagem) {
		super(menssagem);
	}
}
