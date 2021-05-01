package br.com.api.controllers.base;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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

import br.com.api.configs.security.jwt.JwtTokenProvider;
import br.com.api.constants.ExceptionsConstantes;
import br.com.api.dtos.UsuarioDTO;
import br.com.api.models.Usuario;
import br.com.api.services.UsuarioService;

@RestController
@RequestMapping("/acesso")
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
	
	@PostMapping("/novo-usuario")
	public UsuarioDTO cadastrar(@Valid @RequestBody UsuarioDTO usuario) {		
		return usuarioService.cadastro(usuario);
	}
}
