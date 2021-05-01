package com.projeto.backend.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.backend.controllers.base.ControllerGenerico;
import com.projeto.backend.dtos.UsuarioDTO;
import com.projeto.backend.services.UsuarioService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Usuarios", description = "Endpoint dos usuarios", tags = "Usuarios")
@RequestMapping("/api/usuarios")
public class UsuarioController extends ControllerGenerico<UsuarioDTO, Long>{
	
	@Autowired
	private UsuarioService service;

	@Override
	public UsuarioDTO atualizar(@RequestBody UsuarioDTO body) {
		return null;
	}

	@Override
	public UsuarioDTO cadastrar(@Valid @RequestBody UsuarioDTO body) {		
		return service.cadastro(body);
	}

	@Override
	public UsuarioDTO consultar(@PathVariable Long id) {
		return service.consultar(id);
	}

	@Override
	public void excluir(@PathVariable Long id) {
		service.excluir(id, null);
	}

	@Override
	public List<UsuarioDTO> listar() {
		return service.consultarTodos();
	}

}
