package br.com.api.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespostaLoginDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String login;
	private String token;

}
