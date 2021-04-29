package com.projeto.backend.controllers.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public abstract class ControllerGenerico<ENTIDADEDTO, ID extends Serializable> {

	@PutMapping("/{id}")
	public abstract ENTIDADEDTO atualizar(ENTIDADEDTO body);

	@PostMapping
	public abstract ENTIDADEDTO cadastrar(ENTIDADEDTO body);

	@GetMapping("/{id}")
	public abstract ENTIDADEDTO consultar(ID id);

	@DeleteMapping("/{id}")
	public abstract void deletar(ID id);

	@GetMapping
	public abstract List<ENTIDADEDTO> listar();
}
