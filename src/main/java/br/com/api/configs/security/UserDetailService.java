package br.com.api.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.api.models.Usuario;
import br.com.api.services.UsuarioService;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UsuarioService service;

	public UserDetailService(UsuarioService service) {
		this.service = service;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = service.consultarPorLogin(login);
		return new UserDetail(usuario);

	}

}
