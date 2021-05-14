package br.com.api.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static br.com.api.models.Perfil.PERFIL_USUARIO;
import static br.com.api.models.Perfil.PERFIL_ADMIN;
import br.com.api.models.Usuario;

public class ValidacaoUtils {

	public static boolean isPerfilValido(String perfil) {
		List<String> listaPerfisValidos = Arrays.asList(PERFIL_USUARIO, PERFIL_ADMIN);
		return listaPerfisValidos.contains(perfil);
	}

	public static boolean isUsuarioValido(Usuario usuario) {
		if (usuario != null && usuario.getId() != null) {
			return true;
		}
		return false;
	}

	public static boolean isCampoStringValido(String campo) {
		if (campo == null || campo.isBlank() || campo.length() < 3 || campo.length() > 20) {
			return false;
		}
		return true;
	}

	public static boolean isEmailValido(String email) {
		boolean isEmailValido = false;
		if (email != null && email.length() > 0) {
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				isEmailValido = true;
			}
		}
		return isEmailValido;
	}

	public static boolean isIdValido(Long id) {
		if (id == null || id < 1) {
			return false;
		}
		return true;
	}

}
