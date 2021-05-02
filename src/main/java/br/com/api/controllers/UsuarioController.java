package br.com.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.controllers.base.ControllerGenerico;
import br.com.api.dtos.UsuarioDTO;
import br.com.api.services.UsuarioService;
import br.com.api.utils.Utils;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Usuarios", description = "Endpoint dos usuarios", tags = "Usuarios")
@RequestMapping("/api/usuarios")
public class UsuarioController extends ControllerGenerico<UsuarioDTO, Long>{
	
	@Autowired
	private UsuarioService service;

	@Override
	public UsuarioDTO atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
		usuarioDTO.setId(id);
		return service.salvarUsuario(usuarioDTO);
	}

	@Override
	public UsuarioDTO cadastrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {		
		return service.criarUsuario(usuarioDTO);
	}

	@Override
	public UsuarioDTO consultar(@PathVariable Long id) {
		return service.consultar(id);
	}

	@Override
	public void excluir(@PathVariable Long id) {
		service.excluir(id, Utils.getUsuarioLogado());
	}

	@Override
	public List<UsuarioDTO> listar() {
		return service.consultarTodos();
	}

}
