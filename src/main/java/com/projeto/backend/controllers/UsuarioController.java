package com.projeto.backend.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.backend.controllers.base.ControllerGenerico;
import com.projeto.backend.dtos.UsuarioDTO;
import com.projeto.backend.services.UsuarioService;

import io.swagger.annotations.Api;

@CrossOrigin
@RestController
@Api(value = "Usuarios", description = "Endpoint dos usuarios", tags = "Usuarios")
@RequestMapping("/usuarios")
public class UsuarioController extends ControllerGenerico<UsuarioDTO, Long>{
	
	@Autowired
	private UsuarioService service;

	@Override
	public UsuarioDTO atualizar(UsuarioDTO body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsuarioDTO cadastrar(@Valid @RequestBody UsuarioDTO body) {
		
		return service.cadastro(body);
	}

	@Override
	public UsuarioDTO consultar(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletar(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UsuarioDTO> listar() {
		// TODO Auto-generated method stub
		return null;
	}
	
		
	

}
