package br.com.api.dtos;

import java.io.Serializable;
import java.util.List;

import br.com.api.models.Perfil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlteracaoPerfilsDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long idUsuario;
	private String login;
	private List<Perfil> perfis;
	private boolean isRemocaoPerfis;
	
	public AlteracaoPerfilsDTO() {
		
	}

	public AlteracaoPerfilsDTO(Long idUsuario, String login, List<Perfil> perfis, boolean isRemocaoPerfis) {
		this.idUsuario = idUsuario;
		this.login = login;
		this.perfis = perfis;
		this.isRemocaoPerfis = isRemocaoPerfis;
	}
}
