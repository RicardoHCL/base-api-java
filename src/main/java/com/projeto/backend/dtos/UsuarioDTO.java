package com.projeto.backend.dtos;

import java.io.Serializable;


import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonPropertyOrder({"id", "nome", "email"})
@JsonIgnoreProperties(value = {"senha", "confirmacaoSenha" }, allowSetters = true )
public class UsuarioDTO implements Serializable {	
		
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Size(min = 3, max = 20)
	private String nome;	
	
	@Email
	private String email;		
	
	@Size(max = 20)
	private String senha;		
	
	@Size(max = 20)
	private String confirmacaoSenha;

}
