package com.projeto.backend.controllers.base;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.backend.configs.security.jwt.JwtTokenProvider;
import com.projeto.backend.constants.ExceptionsConstantes;
import com.projeto.backend.dtos.UsuarioDTO;
import com.projeto.backend.models.Usuario;
import com.projeto.backend.services.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/login")
	@SuppressWarnings("rawtypes")
	public ResponseEntity criarSessaoLogin(@RequestBody UsuarioDTO usuarioDTO) {
		try {

			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha()));

			Usuario usuario = usuarioService.consultarPorEmail(usuarioDTO.getEmail());
			String token = tokenProvider.criarToken(usuario);

			Map<Object, Object> response = new HashMap<>();
			response.put("email", usuario.getEmail());
			response.put("token", token);
			return ok(response);

		} catch (AuthenticationException ex) {
			throw new BadCredentialsException(ExceptionsConstantes.USUARIO_OU_SENHA_INCORRETO);
		}

	}
}
