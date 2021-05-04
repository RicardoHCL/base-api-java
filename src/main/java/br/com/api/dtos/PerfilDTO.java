package br.com.api.dtos;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.api.constants.ValidacaoConstantes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "idUsuario", "login", "perfis" })
public class PerfilDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idUsuario;
	private String login;

	@NotNull(message = ValidacaoConstantes.PERFIL_OBRIGATORIO)
	@NotEmpty(message = ValidacaoConstantes.PERFIL_OBRIGATORIO)
	private List<String> perfis;
	
	public PerfilDTO() {
		
	}
			
	public PerfilDTO(Long idUsuario, String login) {
		this.idUsuario = idUsuario;
		this.login = login;
	}
	
}
