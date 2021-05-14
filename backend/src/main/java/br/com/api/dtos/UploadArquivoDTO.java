package br.com.api.dtos;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadArquivoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String  nomeArquivo;
	private String UriArquivo;
	private String tipoArquivo;
	private Long tamanho;
	
	public UploadArquivoDTO() {
		
	}
	
	public UploadArquivoDTO(String nomeArquivo, String uriArquivo, String tipoArquivo, Long tamanho) {
		this.nomeArquivo = nomeArquivo;
		UriArquivo = uriArquivo;
		this.tipoArquivo = tipoArquivo;
		this.tamanho = tamanho;
	}
}
