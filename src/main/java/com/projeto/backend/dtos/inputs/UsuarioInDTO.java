package com.projeto.backend.dtos.inputs;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioInDTO {	
	
	private Long id;	
	private String nome;		
	private String email;	
	private String senha;	
	private String confirmacaoSenha;

}
