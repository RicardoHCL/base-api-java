package br.com.api.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.api.configs.security.jwt.JwtConfigurer;
import br.com.api.configs.security.jwt.JwtTokenProvider;
import static br.com.api.models.Perfil.PERFIL_USUARIO;
import static br.com.api.models.Perfil.PERFIL_ADMIN;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	protected void configure(HttpSecurity http) throws Exception {
		http
		.httpBasic().disable()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		.antMatchers("/acesso/**").permitAll()
		.antMatchers(HttpMethod.POST,"/api/usuarios").permitAll()	
		.antMatchers("/admin/**").hasAuthority(PERFIL_ADMIN)
		.antMatchers("/api/**").hasAnyAuthority(PERFIL_USUARIO, PERFIL_ADMIN)
		.and().apply(new JwtConfigurer(this.tokenProvider));
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncode() {
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
		return encode;
	}
}
