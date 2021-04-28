package com.projeto.backend.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.backend.controllers.base.ControllerGenerico;
import com.projeto.backend.converter.DozerConverter;
import com.projeto.backend.dtos.inputs.UsuarioInDTO;
import com.projeto.backend.dtos.outputs.UsuarioOutDTO;
import com.projeto.backend.exceptions.CustomException;
import com.projeto.backend.models.Usuario;
import com.projeto.backend.services.UsuarioService;

@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController extends ControllerGenerico<Usuario, UsuarioInDTO, UsuarioOutDTO, Long>{
	
	@Autowired
	private UsuarioService service;
	
	@Override
	public UsuarioOutDTO atualizar(UsuarioInDTO body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsuarioOutDTO cadastrar(@Valid @RequestBody UsuarioInDTO body) {

		Usuario usuario = converterRequestParaModel(body);
		usuario = service.salvar(usuario);
		
		return converterModelParaResponse(usuario);
	}

	@Override
	public UsuarioOutDTO consultar(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletar(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UsuarioOutDTO> listar() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Usuario converterRequestParaModel(UsuarioInDTO request) throws CustomException {		
		return DozerConverter.converterObjeto(request, Usuario.class);
	}
	
	@Override
	public UsuarioOutDTO converterModelParaResponse(Usuario model) throws CustomException {
		return DozerConverter.converterObjeto(model, UsuarioOutDTO.class);
	}

}
