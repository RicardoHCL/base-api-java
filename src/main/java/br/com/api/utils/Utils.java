package br.com.api.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.api.configs.security.UserDetail;
import br.com.api.models.Usuario;

public class Utils {

	public static String gerarHashSenha(final String senha) {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		return bCrypt.encode(senha);
	}

	public static Usuario getUsuarioLogado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null){
			UserDetail userDetail = (UserDetail) authentication.getPrincipal();
			return userDetail.getUsuario();
		}
		return null;
	}
	
	
}
