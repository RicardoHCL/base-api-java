package com.projeto.backend.configs.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.projeto.backend.configs.security.UserDetailService;
import com.projeto.backend.exceptions.AuthenticationJwtException;
import com.projeto.backend.models.Perfil;
import com.projeto.backend.models.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret}")
	private String chave = "secret";

	@Value("${security.jwt.token.expire-lenght:60}")
	private long validadeEmMinutos = 60;

	@Autowired
	private UserDetailService service;

	@PostConstruct
	public void init() {
		chave = Base64.getEncoder().encodeToString(chave.getBytes());
	}

	public String criarToken(Usuario usuario) {		
		Claims claims = Jwts.claims().setSubject(usuario.getEmail());
		claims.put("roles", getPerfis(usuario.getListaPerfis()));
		
		LocalDateTime horaAtual = LocalDateTime.now();
		LocalDateTime horaExpiracaoToken = horaAtual.plusMinutes(this.validadeEmMinutos);
						
		return Jwts.builder().setClaims(claims)
				.setIssuedAt(Date.from(horaAtual.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(horaExpiracaoToken.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, chave)
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails user = this.service.loadUserByUsername(getEmailUsuario(token));
		return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
	}
	
	public String getToken(HttpServletRequest request) {		
		String beareToken = request.getHeader("Authorization");
		
		if(beareToken != null && beareToken.startsWith("Bearer ")) {
			return beareToken.substring(7, beareToken.length());
		}
		
		return null;
	}
	
	public boolean isTokenValido(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(chave).parseClaimsJws(token);
			
			if(claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			
			return true;
		}catch(Exception exception) {
			throw new AuthenticationJwtException("");
		}
	}
	
	
	// Recuperar o email apartir do token
	private String getEmailUsuario(String token) {
		return Jwts.parser().setSigningKey(chave).parseClaimsJws(token).getBody().getSubject();
	}

 	private List<String> getPerfis(List<Perfil> listaPerfis) {
		List<String> perfis = new ArrayList<>();
		
		for (Perfil perfil : listaPerfis) {
			perfis.add(perfil.getNome().getDescricao());
		}
		
		return perfis;
	}

}
