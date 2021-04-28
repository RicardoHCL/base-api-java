package com.projeto.backend.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.backend.models.Usuario;
import com.projeto.backend.repositories.UsuarioRepository;
import com.projeto.backend.services.base.ServiceGenerico;

@Service
public class UsuarioService extends ServiceGenerico<Usuario, Long, UsuarioRepository> {

	@Autowired
	private UsuarioRepository repository;
	
	
	@Override
	public UsuarioRepository getRepositorio() {	
		return repository;
	}
	
	public Usuario salvar(Usuario usuario) {
		return salvar(usuario, null);
	}
	
	
}
