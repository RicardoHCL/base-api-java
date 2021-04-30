package com.projeto.backend.configs.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.backend.constants.MenssagensErrosConstantes;
import com.projeto.backend.models.Usuario;
import com.projeto.backend.repositories.UsuarioRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	public UserDetailService(UsuarioRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findDistinctByEmailAndAtivo(email, true);
		usuario.orElseThrow(() -> new UsernameNotFoundException(MenssagensErrosConstantes.EMAIL_INVALIDO));
		return new UserDetail(usuario.get());
	}

}
