package br.com.api.dtos;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.api.constants.ValidacaoConstantes;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonPropertyOrder({"id", "nome", "email"})
@JsonIgnoreProperties(value = {"senha", "confirmacaoSenha", "novaSenha" }, allowSetters = true )
public class UsuarioDTO implements Serializable {	
		
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message = ValidacaoConstantes.EMAIL_OBRIGATORIO)
	@NotEmpty(message = ValidacaoConstantes.EMAIL_OBRIGATORIO)
	@Size(min = 3, max = 20, message = "O nome não pode ser menor que 3")
	private String nome;	
	
	@NotNull(message = ValidacaoConstantes.EMAIL_OBRIGATORIO)
	@NotEmpty(message = ValidacaoConstantes.EMAIL_OBRIGATORIO)
    @Email(message = ValidacaoConstantes.EMAIL_INVALIDO)
	private String email;		
	
	@NotNull(message = ValidacaoConstantes.SENHA_OBRIGATORIA)
	@NotEmpty(message = ValidacaoConstantes.SENHA_OBRIGATORIA)
	@Size(min = 6, max = 20, message = ValidacaoConstantes.SENHA_INVALIDA)
	private String senha;		
	
	@NotNull(message = ValidacaoConstantes.CONFIRMA_SENHA_OBRIGATORIA)
	@NotEmpty(message = ValidacaoConstantes.CONFIRMA_SENHA_OBRIGATORIA)
	@Size(min = 6, max = 20, message = ValidacaoConstantes.CONFIRMA_SENHA_INVALIDA)
	private String confirmacaoSenha;
	
	//Apenas em caso de alteracao de senha
	private String novaSenha;
	
}
