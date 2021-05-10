package br.com.api.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.api.models.Usuario;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository repository;

	@Test
	public void criarUsuario() {
		Usuario usuario = new Usuario("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456");
		this.repository.save(usuario);

		assertThat(usuario.getId()).isNotNull();
		assertThat(usuario.getNome()).isEqualTo("Ricardo Lima");
		assertThat(usuario.getLogin()).isEqualTo("ricardo");
		assertThat(usuario.getEmail()).isEqualTo("ricardo@gmail.com");
		assertThat(usuario.getSenha()).isEqualTo("123456");
	}

	@Test
	public void deletarUsuario() {
		Usuario usuario = new Usuario("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456");
		this.repository.save(usuario);

		this.repository.deleteById(usuario.getId());

		try {
			this.repository.findById(usuario.getId()).get();
		} catch (Exception e) {
			assertThat(NoSuchElementException.class).isEqualTo(e.getClass());
			assertThat("No value present").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void consultarUsuario() {
		Usuario usuario = new Usuario("Ricardo Lima", "ricardo", "ricardo@gmail.com", "123456");
		this.repository.save(usuario);

		Usuario usuarioBanco = this.repository.findById(usuario.getId()).get();

		assertThat(usuarioBanco.getId()).isEqualTo(usuario.getId());
		assertThat(usuarioBanco.getNome()).isEqualTo("Ricardo Lima");
		assertThat(usuarioBanco.getLogin()).isEqualTo("ricardo");
		assertThat(usuarioBanco.getEmail()).isEqualTo("ricardo@gmail.com");
		assertThat(usuarioBanco.getSenha()).isEqualTo("123456");
	}
}
