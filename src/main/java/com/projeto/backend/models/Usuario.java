package com.projeto.backend.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.projeto.backend.enums.PerfilEnum;
import com.projeto.backend.models.base.Pojo;

import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Table(name = "usuarios")
@Getter @Setter
public class Usuario extends Pojo<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "senha", nullable = false)
	private String senha;	
		
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "usuario_perfil", joinColumns = {@JoinColumn(name = "id_usuario")}, 
	inverseJoinColumns = {@JoinColumn(name = "id_perfil")})
	private List<Perfil> listaPerfis;
	
	
	//Por padrao todos todos usuarios inicia com esse perfil
	public List<Perfil> getListaPerfis() {
		if( this.listaPerfis == null || this.listaPerfis.isEmpty()) {
			this.listaPerfis = new ArrayList<>();
			this.listaPerfis.add(new Perfil(PerfilEnum.USUARIO));
		}
		
		return this.listaPerfis;
	}

}
