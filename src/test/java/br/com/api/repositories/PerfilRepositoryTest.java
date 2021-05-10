package br.com.api.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.api.models.Perfil;

@DataJpaTest
@ActiveProfiles("test")
public class PerfilRepositoryTest {

	@Autowired
	private PerfilRepository repository;

	@Test
	public void criarPerfil() {
		Perfil perfil = new Perfil(Perfil.PERFIL_USUARIO);
		this.repository.save(perfil);

		assertThat(perfil.getId()).isNotNull();
		assertThat(perfil.getNome()).isEqualTo("USUARIO");
	}

	@Test
	public void deletarPerfil() {
		Perfil perfil = new Perfil(Perfil.PERFIL_USUARIO);
		this.repository.save(perfil);

		this.repository.deleteById(perfil.getId());
		try {
			this.repository.findById(perfil.getId()).get();
		} catch (Exception e) {
			assertThat(NoSuchElementException.class).isEqualTo(e.getClass());
			assertThat("No value present").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void consultarPerfil() {
		Perfil perfil = new Perfil(Perfil.PERFIL_ADMIN);
		this.repository.save(perfil);

		Perfil perfilBanco = this.repository.findById(perfil.getId()).get();

		assertThat(perfilBanco.getId()).isEqualTo(perfil.getId());
		assertThat(perfilBanco.getNome()).isEqualTo("ADMINISTRADOR");
	}

}
