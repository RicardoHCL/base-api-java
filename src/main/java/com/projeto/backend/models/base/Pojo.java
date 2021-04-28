package com.projeto.backend.models.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.backend.models.Usuario;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Audited
@MappedSuperclass
@EqualsAndHashCode
@Getter @Setter
public abstract class Pojo<ID extends Serializable> implements IEntidade<ID> {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "ativo", nullable = false)
	private boolean ativo;
	
	@Column(name = "data_alteracao", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
	private LocalDateTime dataAlteracao;
	
	@Column(name = "data_exclusao")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
	private LocalDateTime dataExclusao;
	
	@Column(name = "data_inclusao", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
	private LocalDateTime dataInclusao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
}
