package br.com.api.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.api.models.Perfil;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Testes unitários de persistencia do perfil")
public class PerfilRepositoryTest {

	@Autowired
	private PerfilRepository repository;
	
	private Perfil perfil;

	@BeforeAll
	@DisplayName("Preparando para iniciar os testes com um perfil salvo no banco")
	public void setUp() {
		Perfil perfilUsuario = new Perfil(Perfil.PERFIL_USUARIO);
		perfilUsuario.setAtivo(true);
		perfilUsuario.setDataInclusao(LocalDateTime.now());
		perfilUsuario.setDataAlteracao(LocalDateTime.now());
		this.perfil = this.repository.save(perfilUsuario);
	}
	
	@Test
	@Order(1)
	@DisplayName("Criando um perfil")
	public void criarPerfil() {
		Perfil perfil = new Perfil(Perfil.PERFIL_ADMIN);
		perfil.setAtivo(true);
		perfil.setDataInclusao(LocalDateTime.now());
		perfil.setDataAlteracao(LocalDateTime.now());
		this.repository.save(perfil);

	
		
		assertThat(perfil.getId()).isNotNull();
		assertThat(perfil.getNome()).isEqualTo("ADMINISTRADOR");
	}

	@Test
	@Order(2)
	@DisplayName("consultando perfil pelo id")
	public void consultarPerfil() {
		Perfil perfil = this.repository.findById(this.perfil.getId()).get();

		assertThat(perfil.getId()).isEqualTo(this.perfil.getId());
		assertThat(perfil.getNome()).isEqualTo("USUARIO");
	}
	
	@Test
	@Order(3)
	@DisplayName("listando os perfis ativos")
	public void listandoPerfisAtivos() {
		List<Perfil> perfis = this.repository.findByAtivo(true);

		assertThat(perfis).isNotEmpty();
		assertThat(perfis.size()).isEqualTo(1);
	}

	@Test
	@Order(5)
	@DisplayName("Deletando perfil e verificando se ele foi deletado")
	public void deletarPerfil() {
		this.repository.deleteById(this.perfil.getId());
		try {
			this.repository.findById(this.perfil.getId()).get();
		} catch (Exception e) {
			assertThat(NoSuchElementException.class).isEqualTo(e.getClass());
			assertThat("No value present").isEqualTo(e.getMessage());
		}
	}
	
	@Test
	@Order(4)
	@DisplayName("consultando perfil ativo pelo nome")
	public void consultarPeloNome() {
		Perfil perfil = this.repository.findDistinctByNomeAndAtivo(Perfil.PERFIL_USUARIO, true).get();
		
		assertThat(perfil.getId()).isEqualTo(this.perfil.getId());
		assertThat(perfil.getNome()).isEqualTo("USUARIO");
	}

}
