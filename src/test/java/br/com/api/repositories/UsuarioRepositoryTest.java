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

import br.com.api.models.Usuario;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Testes unitários de persistencia do usuário")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository repository;

	private Usuario usuario;

	@BeforeAll
	@DisplayName("Preparando para iniciar os testes com usuarios salvos no banco")
	public void setUp() {
		Usuario usuarioUm = new Usuario("Teste", "teste", "teste@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		usuarioUm.setAtivo(true);
		usuarioUm.setDataInclusao(LocalDateTime.now());
		usuarioUm.setDataAlteracao(LocalDateTime.now());
		this.usuario = this.repository.save(usuarioUm);

		Usuario usuarioDois = new Usuario("Fernando", "nando_", "nando@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		usuarioDois.setAtivo(true);
		usuarioDois.setDataInclusao(LocalDateTime.now());
		usuarioDois.setDataAlteracao(LocalDateTime.now());
		this.repository.save(usuarioDois);
	}

	@Test
	@Order(1)
	@DisplayName("Criando usuário")
	public void criarUsuario() {
		Usuario usuario = new Usuario("Ricardo Lima", "ricardo", "ricardo@gmail.com",
				"$2a$10$A3BtshmFkCkcmWkDLfzA6OoS0xIEVPvc/rh2lbITuzoNqSFHjuizC");
		usuario.setAtivo(true);
		usuario.setDataInclusao(LocalDateTime.now());
		usuario.setDataAlteracao(LocalDateTime.now());
		this.repository.save(usuario);

		assertThat(usuario.getId()).isNotNull();
		assertThat(usuario.getNome()).isEqualTo("Ricardo Lima");
		assertThat(usuario.getLogin()).isEqualTo("ricardo");
		assertThat(usuario.getEmail()).isEqualTo("ricardo@gmail.com");
	}

	@Test
	@Order(7)
	@DisplayName("Deletando usuário")
	public void deletarUsuario() {
		this.repository.deleteById(this.usuario.getId());

		try {
			this.repository.findById(this.usuario.getId()).get();
		} catch (Exception e) {
			assertThat(NoSuchElementException.class).isEqualTo(e.getClass());
			assertThat("No value present").isEqualTo(e.getMessage());
		}
	}

	@Test
	@Order(2)
	@DisplayName("Consultando usuário pelo id")
	public void consultarUsuario() {
		Usuario usuario = this.repository.findById(this.usuario.getId()).get();

		assertThat(usuario.getId()).isEqualTo(this.usuario.getId());
		assertThat(usuario.getNome()).isEqualTo("Teste");
		assertThat(usuario.getLogin()).isEqualTo("teste");
		assertThat(usuario.getEmail()).isEqualTo("teste@gmail.com");
	}
	
	@Test
	@Order(3)
	@DisplayName("Consultando usuário ativo pelo id")
	public void consultarUsuarioAtivo() {
		Usuario usuario = this.repository.findDistinctByIdAndAtivo(this.usuario.getId(), true).get();

		assertThat(usuario.getId()).isEqualTo(this.usuario.getId());
		assertThat(usuario.getNome()).isEqualTo("Teste");
		assertThat(usuario.getLogin()).isEqualTo("teste");
		assertThat(usuario.getEmail()).isEqualTo("teste@gmail.com");
	}
	
	@Test
	@Order(4)
	@DisplayName("Contando usuários ativos pelo email")
	public void contarUsuariosAtivosPeloEmail() {
		Long qtdUsuarios = this.repository.countByEmailAndIdNot(this.usuario.getEmail(), this.usuario.getId());

		assertThat(qtdUsuarios).isEqualTo(0);
	}
	
	@Test
	@Order(5)
	@DisplayName("Contando usuários ativos pelo login")
	public void contarUsuariosAtivosPeloLogin() {
		Long qtdUsuarios = this.repository.countByLoginAndIdNot("nando_", this.usuario.getId());

		assertThat(qtdUsuarios).isEqualTo(1);
	}
	
	@Test
	@Order(6)
	@DisplayName("Contando usuários ativos")
	public void contarUsuariosAtivos() {
		Long qtdUsuarios = this.repository.countByAtivo(true);

		assertThat(qtdUsuarios).isEqualTo(2);
	}
	
	@Test
	@Order(8)
	@DisplayName("Listando usuários ativos")
	public void listarUsuariosAtivos() {
		List<Usuario> usuarios = this.repository.findByAtivo(true);

		assertThat(usuarios).isNotEmpty();
		assertThat(usuarios.size()).isEqualTo(2);
	}
	
	@Test
	@Order(9)
	@DisplayName("Consultando usuário ativo pelo login")
	public void consultaUsuarioAtivoPeloLogin() {
		Usuario usuario = this.repository.findDistinctByLoginAndAtivo("nando_", true).get();

		assertThat(usuario.getId()).isNotNull();
		assertThat(usuario.getNome()).isEqualTo("Fernando");
		assertThat(usuario.getLogin()).isEqualTo("nando_");
		assertThat(usuario.getEmail()).isEqualTo("nando@gmail.com");;
	}
	
}
