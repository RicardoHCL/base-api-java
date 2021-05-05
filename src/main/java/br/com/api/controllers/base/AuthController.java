package br.com.api.controllers.base;

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

import br.com.api.configs.security.jwt.JwtTokenProvider;
import br.com.api.constants.ExceptionsConstantes;
import br.com.api.dtos.UsuarioDTO;
import br.com.api.models.Usuario;
import br.com.api.services.UsuarioService;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Login", description = "Endpoint para efetuar o login", tags = "Login")
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
					new UsernamePasswordAuthenticationToken(usuarioDTO.getLogin(), usuarioDTO.getSenha()));

			Usuario usuario = usuarioService.consultarPorLogin(usuarioDTO.getLogin());
			String token = tokenProvider.criarToken(usuario);

			Map<Object, Object> response = new HashMap<>();
			response.put("login", usuario.getLogin());
			response.put("token", token);
			return ok(response);

		} catch (AuthenticationException ex) {
			throw new BadCredentialsException(ExceptionsConstantes.USUARIO_OU_SENHA_INCORRETO);
		}
	}
}
