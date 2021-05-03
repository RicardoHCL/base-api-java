package br.com.api.enums;

public enum PerfilEnum {

	ADMINISTRADOR("ADMINISTRADOR"), USUARIO("USUARIO");
	
	private String descricao;
	
	private PerfilEnum (final String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
