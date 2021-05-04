package br.com.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import br.com.api.models.base.Pojo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Table(name = "perfis")
@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class Perfil extends Pojo<Long> implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	public static final String PERFIL_USUARIO = "USUARIO";
	public static final String PERFIL_ADMIN = "ADMINISTRADOR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pefil")
	@SequenceGenerator(name = "seq_pefil", sequenceName = "seq_pefil", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome", unique = true)
	private String nome;

	/**
	 * Construtores
	 */
	public Perfil() {
	}

	public Perfil(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Perfil(String nome) {
		this.nome = nome;
	}

	@Override
	public String getAuthority() {
		return nome;
	}

}
