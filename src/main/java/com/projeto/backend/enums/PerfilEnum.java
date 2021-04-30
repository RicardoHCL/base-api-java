package com.projeto.backend.enums;

public enum PerfilEnum {

	ADMINISTRADOR("ADMIN"), USUARIO("USUARIO");
	
	private String descricao;
	
	private PerfilEnum (final String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
