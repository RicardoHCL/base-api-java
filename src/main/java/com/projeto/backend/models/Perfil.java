package com.projeto.backend.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import com.projeto.backend.enums.PerfilEnum;
import com.projeto.backend.models.base.Pojo;

import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Table(name = "perfis")
@Getter @Setter
public class Perfil extends Pojo<Long> implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pefil")
	@SequenceGenerator(name = "seq_pefil", sequenceName = "seq_pefil", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "nome", unique = true)
	private PerfilEnum nome;

	/**
	 * Construtores
	 */
	public Perfil() {
	}

	public Perfil(Long id, PerfilEnum nome) {
		this.id = id;
		this.nome = nome;
	}

	public Perfil(PerfilEnum nome) {
		this.nome = nome;
	}

	@Override
	public String getAuthority() {
		return nome.getDescricao();
	}

}
