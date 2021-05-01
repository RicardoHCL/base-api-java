package com.projeto.backend.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.backend.models.Usuario;
import com.projeto.backend.services.UsuarioService;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UsuarioService service;

	public UserDetailService(UsuarioService service) {
		this.service = service;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = service.consultarPorEmail(email);
		return new UserDetail(usuario);

	}

}
