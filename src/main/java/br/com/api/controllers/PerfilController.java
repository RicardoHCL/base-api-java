package br.com.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.dtos.PerfilDTO;
import br.com.api.services.PerfilService;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Perfis", description = "Endpoint dos perfis", tags = "Perfis")
@RequestMapping("/admin/perfis")
public class PerfilController {

	@Autowired
	private PerfilService service;

	@PostMapping("/adicionar")
	public PerfilDTO adicionarPerfilAoUsuario(@Valid @RequestBody PerfilDTO perfil) {
		return this.service.alterarPerfil(perfil, false);
	}

	@PostMapping("/remover")
	public PerfilDTO removerPerfilDoUsuario(@Valid @RequestBody PerfilDTO perfil) {
		return this.service.alterarPerfil(perfil, true);
	}

}
