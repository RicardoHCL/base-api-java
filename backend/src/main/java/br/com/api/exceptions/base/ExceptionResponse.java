package br.com.api.exceptions.base;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dataHora;
	private String menssagem;
	private String endpoint;	
	
	public ExceptionResponse(String dataHora, String menssagem, String endpoint) {
		super();
		this.dataHora = dataHora;
		this.menssagem = menssagem;
		this.endpoint = endpoint;
	}	

}
