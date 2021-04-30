package com.projeto.backend.controllers.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.backend.configs.security.jwt.JwtTokenProvider;
import com.projeto.backend.models.Usuario;
import com.projeto.backend.repositories.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	public ResponseEntity criarSessaoLogin(@RequestBody Usuario usuario) {
		try {
			
			authManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha()));
			
			usuario = usuarioRepository.findDistinctByEmailAndAtivo(usuario.getEmail(), true).get();
			
			String token = "";
			
			if(usuario == null || usuario.getId() == null) {
				throw new UsernameNotFoundException("");
			}
			
			token = tokenProvider.criarToken(usuario);
			
			Map<Object, Object> response = new HashMap<>();
			response.put("email", usuario.getEmail());
			response.put("token", token);
			return ok(response);
			
		}catch(AuthenticationException ex) {
			throw new BadCredentialsException("");
		}
	}

}
